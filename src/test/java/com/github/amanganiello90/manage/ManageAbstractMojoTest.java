package com.github.amanganiello90.manage;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;

public abstract class ManageAbstractMojoTest {

	protected static String projectDir;

	@Rule
	public MojoRule rule = new MojoRule();

}
