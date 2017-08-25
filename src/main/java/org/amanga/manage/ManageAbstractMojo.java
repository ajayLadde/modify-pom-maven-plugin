package org.amanga.manage;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom abstract class
 * 
 * @author amanganiello90
 * 
 */
public abstract class ManageAbstractMojo extends AbstractMojo {

	/**
	 * factory logger
	 */
	protected Logger logger = LoggerFactory.getLogger("");

	/**
	 * common variables
	 */
	protected String dir = System.getProperty("user.dir"); // current directory
	protected String testPom = null;

	/**
	 * property related if you have a git repo
	 */
	@Parameter(property = "gitRepo", defaultValue = "false")
	protected String gitRepo;

	/**
	 * property related what git branch to process
	 */
	@Parameter(property = "gitBranch", defaultValue = "master")
	protected String gitBranch;

	/**
	 * property if you want to commit
	 */
	@Parameter(property = "gitCommit", defaultValue = "false")
	protected String gitCommit;

	/**
	 * method to initialize context for set properties
	 * 
	 * @throws Exception
	 *             for any problems
	 */
	protected void initialize() throws Exception {

		if (("false").equals(gitRepo) && ("true").equals(gitCommit)) {
			throw new IllegalStateException("You have set the gitCommit parameter to true and gitRepo to false!");

		}

		if (("false").equals(gitRepo)) {

			logger.info("----------------CHECK ON '" + gitBranch + "' BRANCH TYPE----------------");

			try {

				Git.open(new File(dir)).checkout().setName(gitBranch).setStartPoint("origin/" + gitBranch)
						.setCreateBranch(true).call();

			} catch (RefNotFoundException e) {

				logger.error("********* You don't have a '" + gitBranch + "' branch for your maven project!");
				throw new Exception("You don't have a valid " + gitBranch + " branch!");

			} catch (RefAlreadyExistsException e) {

				// branch already exist
				Git.open(new File(dir)).checkout().setName(gitBranch).call();

			}
		}

	}

	/**
	 * method to set pom name for junit test
	 *
	 * @param testPom:
	 *            name of test pom	
	 */
	protected void setTestPom(String pomName) {
		this.testPom = pomName;

	}

}
