package org.amanga.managecore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * class for check SNAPSHOT dependency on pom.xml
 * 
 * @author amanganiello
 */
public class ReadPoms {

	private static final String SNAPSHOT = "SNAPSHOT";
	private static final String VERSION = "version";
	private static final String PARENT = "parent";
	private static final String MODULE = "module";
	private static final String MODULES = "modules";
	private static final String PROPERTIES = "properties";
	private static final String ALL = "all";
	private static final String DEPENDENCIES = "dependencies";

	/**
	 * Constructor
	 */
	private ReadPoms() {
	}

	/**
	 * read poms and return an expection if a SNAPSHOT dependency exist
	 * 
	 * @param directory
	 * @param typology:
	 *            all, parent or dependencies
	 * @return null if there aren't SNAPSHOTS
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String checkSnapshots(String directory, String typology)
			throws ParserConfigurationException, SAXException, IOException {

		if (!((ALL).equals(typology) || (PARENT).equals(typology) || (DEPENDENCIES).equals(typology))) {
			throw new IllegalArgumentException(
					"Illegal argument value for typology: set to 'all', 'dependency' or 'parent'");
		}

		String pomName = directory + "/pom.xml";

		// Reading your main pom
		File pomFile = new File(pomName);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		org.w3c.dom.Document document = documentBuilder.parse(pomFile);

		if ((ALL).equals(typology) || (PARENT).equals(typology)) {
			if (ReadPoms.checkSnapshotParent(document)) {
				throw new IllegalStateException("YOU HAVE A SNAPSHOT VERSION IN THE PARENT OF YOUR POM! : " + pomName);

			}
		}

		if ((ALL).equals(typology) || (DEPENDENCIES).equals(typology)) {

			// check submodules
			List<String> modules = ReadPoms.getMavenSubmodules(document);
			if (ReadPoms.checkSnapshotProperties(document) || ReadPoms.checkSnapshotVersions(document)) {
				throw new IllegalStateException("YOU HAVE SNAPSHOT VERSIONS ON YOUR MAIN POM! : " + pomName);
			}

			String childMessage = ReadPoms.checkChildSnapshotVersions(directory, modules, documentBuilder);

			if (childMessage != null) {
				throw new IllegalStateException(childMessage);
			}

		}

		return null;

	}

	/**
	 * read parent pom version and return true if a SNAPSHOT dependency exists
	 * 
	 * @param document
	 * @return
	 * 
	 */
	private static boolean checkSnapshotParent(org.w3c.dom.Document document) {

		// get the parent version
		int exist = document.getElementsByTagName(PARENT).getLength();

		if (exist == 1) {

			Node node = document.getElementsByTagName(PARENT).item(0);

			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				String nodeName = children.item(i).getNodeName();
				if ((VERSION).equals(nodeName)) {

					if (children.item(i).getTextContent().contains(SNAPSHOT)) {

						return true;
					}

				}
			}
		}

		return false;

	}

	/**
	 * check maven submodules
	 * 
	 * @param document
	 * @return
	 * 
	 */
	private static List<String> getMavenSubmodules(org.w3c.dom.Document document) {

		List<String> result = new ArrayList<String>();

		// get the the submodules name
		int exist = document.getElementsByTagName(MODULE).getLength();

		for (int i = 0; i < exist; i++) {
			Node node = document.getElementsByTagName(MODULE).item(i);
			if(MODULES.equals(node.getParentNode())){
			result.add(node.getTextContent());
			}
		}

		return result;

	}

	/**
	 * check snapshot versions
	 * 
	 * @param document
	 * @return
	 * 
	 */
	private static boolean checkSnapshotVersions(org.w3c.dom.Document document) {

		// get the versions
		int exist = document.getElementsByTagName(VERSION).getLength();
		for (int i = 0; i < exist; i++) {

			Node node = document.getElementsByTagName(VERSION).item(i);
			if (node.getTextContent().contains(SNAPSHOT) && !(PARENT).equals(node.getParentNode().getNodeName())) {
				return true;
			}

		}

		return false;

	}

	/**
	 * check snapshot versions on properties
	 * 
	 * @param document
	 * @return
	 * 
	 */
	private static boolean checkSnapshotProperties(org.w3c.dom.Document document) {

		// get the versions
		int exist = document.getElementsByTagName(PROPERTIES).getLength();
		if (exist == 1) {

			Node node = document.getElementsByTagName(PROPERTIES).item(0);
			NodeList children = node.getChildNodes();

			int numberChildren = children.getLength();

			for (int i = 0; i < numberChildren; i++) {

				String propertyVersion = children.item(i).getTextContent();
				if (ReadPoms.isCommentedTag(document, children.item(i).getNodeName())) {
					continue;
				}
				if (propertyVersion.contains(SNAPSHOT)) {
					return true;
				}

			}

		}

		return false;

	}

	/**
	 * check snapshot versions on child poms
	 * 
	 * @param directory
	 * @param modules
	 * @param documentBuilder
	 * @return null if there aren't SNAPSHOTS
	 * @throws IOException
	 * @throws SAXException
	 * 
	 */
	private static String checkChildSnapshotVersions(String directory, List<String> modules,
			DocumentBuilder documentBuilder) throws SAXException, IOException {

		for (int i = 0; i < modules.size(); i++) {
			// Reading your child pom
			String pomNameChild = directory + "/" + modules.get(i) + "/pom.xml";
			File pomChildFile = new File(pomNameChild);
			org.w3c.dom.Document documentChild = documentBuilder.parse(pomChildFile);

			// check submodules
			List<String> modulesChild = ReadPoms.getMavenSubmodules(documentChild);

			if (ReadPoms.checkSnapshotProperties(documentChild) || ReadPoms.checkSnapshotVersions(documentChild)) {
				return "YOU HAVE SNAPSHOT VERSIONS ON YOUR CHILD POM! : " + pomNameChild;
			}

			if (!modulesChild.isEmpty()) {

				return ReadPoms.checkChildSnapshotVersions(directory + "/" + modules.get(i), modulesChild,
						documentBuilder);
			}

		}

		return null;

	}

	/**
	 * check properties commented in pom.xml
	 * 
	 * @param document
	 * @param tagName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static boolean isCommentedTag(org.w3c.dom.Document document, String tagName) {

		try {
			document.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (NullPointerException e) {
			return true;
		}

		return false;

	}

}
