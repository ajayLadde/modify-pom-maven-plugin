package org.amanga.managecore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * class to manipulate properties file
 * 
 * @author amanganiello90
 */
public class PropertiesUtil {

	private static final String PARENT = "parent";
	private static final String VERSION = "version";
	private static final String PROPERTIES = "properties";
	private static final String PARENT_ARTIFACT = "parentArtifact";
	private static final String PARENT_VERSION = "parentVersion";
	private static final String ARTIFACTID = "artifactId";
	private static final String PARENT_TOKEN = ".PARENT";

	/**
	 * Constructor
	 */
	private PropertiesUtil() {
	}

	/**
	 * factory logger
	 */
	private static Logger logger = LoggerFactory.getLogger("");

	/**
	 * method to get properties from a file
	 * 
	 * @param propertiesFilePath
	 *            of your path property file
	 * @throws Exception
	 *             for any problems
	 * 
	 * @return Properties object
	 */
	public static Properties getPropertiesFile(String propertiesFilePath) throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(propertiesFilePath);

			// load a properties file
			prop.load(input);
		} catch (FileNotFoundException e) {
			logger.error("**********The " + propertiesFilePath + " file is not found in your current dir");
			throw new FileNotFoundException(propertiesFilePath + " file not found!!");
		} catch (IOException e) {
			logger.error("**********Error to read the " + propertiesFilePath + " file");
			throw new IOException(propertiesFilePath + " file not readable!!");
		} finally {
			if (input != null) {
				input.close();
			}
		}

		return prop;

	}

	/**
	 * set fixed properties on POM
	 * 
	 * @param locationModule
	 *            of your project
	 * @param prop
	 *            for your pom.xml
	 * @param parentArtifactIdName
	 *            of your parent pom.xml
	 * @param parentVersionValue
	 *            of your parent pom.xml
	 * @param gitCommit
	 *            to commit
	 * @throws Exception
	 *             for any problems
	 */
	public static void setPropertiesOnPom(String locationModule, Properties prop, String parentArtifactIdName,
			String parentVersionValue, String gitCommit) throws Exception {

		// Reading your pom
		File pomFile = new File(locationModule + "/pom.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		org.w3c.dom.Document document = documentBuilder.parse(pomFile);
		boolean commit = false;

		// set parent to new version (if exist)

		// get the parent artifactId
		int exist = document.getElementsByTagName(PARENT).getLength();

		if (exist == 1) {

			Node node = document.getElementsByTagName(PARENT).item(0);

			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				String nodeName = children.item(i).getNodeName();
				if ((ARTIFACTID).equals(nodeName) && children.item(i).getTextContent().equals(parentArtifactIdName)) {
					for (int j = 0; j < children.getLength(); j++) {
						String nodeNameMatched = children.item(j).getNodeName();
						if ((VERSION).equals(nodeNameMatched)) {

							if (parentVersionValue != null) {
								if (!(children.item(j).getTextContent().equals(parentVersionValue))) {
									children.item(j).setTextContent(parentVersionValue);
									logger.info("**********Update parent '" + parentArtifactIdName + "' on "
											+ locationModule + " module to " + parentVersionValue + " version");
									commit = true;
								}
							}
						}

					}

				}

			}

		}

		// set properties
		exist = document.getElementsByTagName(PROPERTIES).getLength();
		if (exist == 1) {

			Node node = document.getElementsByTagName(PROPERTIES).item(0);
			NodeList children = node.getChildNodes();

			int numberChildren = children.getLength();

			for (int i = 0; i < numberChildren; i++) {

				String propertyNodeName = children.item(i).getNodeName();

				if (PropertiesUtil.isCommentedTag(document, propertyNodeName)) {
					continue;
				}
				String propertyValue = prop.getProperty(propertyNodeName);
				if (propertyValue != null) {
					if (!(children.item(i).getTextContent().equals(propertyValue))) {
						children.item(i).setTextContent(propertyValue);
						logger.info("**********Update property '" + propertyNodeName + "' on " + locationModule
								+ " module to " + propertyValue + " version");
						commit = true;
					}

				}

			}

		}

		if (commit && ("true").equals(gitCommit)) {
			// write the content into develop pom file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(pomFile);
			transformer.transform(source, result);

			// commit this change on develop
			Git repo = Git.open(new File(locationModule));
			repo.add().addFilepattern("pom.xml").call();
			repo.commit().setAuthor("manage-pom-maven-plugin", "github@example.com")
					.setMessage("update properties with properties-set goal").call();

		}

	}

	/**
	 * check properties commented in pom.xml
	 * 
	 * @param document
	 *            of your pom.xml
	 * @param tagName
	 *            of your pom.xml
	 * @throws ParserConfigurationException
	 *             if pom.xml is not parsed
	 * @throws SAXException
	 *             for pom.xml
	 * @throws IOException
	 *             for pom.xml
	 */
	private static boolean isCommentedTag(org.w3c.dom.Document document, String tagName) {

		try {
			document.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (NullPointerException e) {
			return true;
		}

		return false;

	}

	/**
	 * get parent properties from file
	 * 
	 * @param prop
	 *            of your pom.xml
	 * @param parentPropValues
	 *            of your parent pom.xml
	 */
	public static void getParentProperty(Properties prop, Map<String, String> parentPropValues) {

		// get all keys
		Set<Object> keys = prop.keySet();

		for (Object key : keys) {
			String currentKey = key.toString();
			// convention is <artifactId>.PARENT

			if (currentKey.contains(PARENT_TOKEN)) {
				String split[] = currentKey.split("\\" + PARENT_TOKEN);
				parentPropValues.put(PARENT_ARTIFACT, split[0]);
				parentPropValues.put(PARENT_VERSION, prop.get(key).toString());
			}

		}

	}

	/**
	 * get artifact parent property from map
	 * 
	 * @param parentPropValues
	 *            of your parent pom.xml
	 * 
	 * @return String artifact of your parent pom.xml
	 */
	public static String getArtifactParentProperty(Map<String, String> parentPropValues) {

		return parentPropValues.get(PARENT_ARTIFACT);

	}

	/**
	 * get version parent property from map
	 * 
	 * @param parentPropValues
	 *            of your parent pom.xml
	 * 
	 * @return String version of your parent pom.xml
	 */
	public static String getVersionParentProperty(Map<String, String> parentPropValues) {

		return parentPropValues.get(PARENT_VERSION);

	}

}
