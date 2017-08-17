package org.amanga.manage;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
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
	protected boolean useGitRepo = false;

	/**
	 * property related if you have a git repo
	 */
	@Parameter(property = "gitRepo", defaultValue = "false")
	protected String gitRepo;

	/**
	 * property related what branch to process
	 */
	@Parameter(property = "gitBranch", defaultValue = "master")
	protected String gitBranch;

	/**
	 * property if you want to commit
	 */
	@Parameter(property = "gitCommit", defaultValue = "false")
	protected String gitCommit;

	protected void initialize() {

		if (("false").equals(gitRepo) && ("true").equals(gitCommit)) {
			throw new IllegalStateException("You have set the gitCommit parameter to true and gitRepo to false!");

		}

	}

}
