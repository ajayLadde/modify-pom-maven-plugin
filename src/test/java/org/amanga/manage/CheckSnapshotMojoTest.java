package org.amanga.manage;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/*
 * This class contains test scenarios for check-snapshots goal
 */
public class CheckSnapshotMojoTest  extends ManageAbstractMojoTest{

	protected static final String GOAL = "check-snapshots";

	@Rule
	public MojoRule rule = new MojoRule();

	@Before
	public final void prepareEnvironment() throws Exception {
		System.out.println("@Before - revertChanges");

	}

	@After
	public final void revertChanges() throws Exception {
		System.out.println("@After - revertChanges");

	}

	@Test
	public void testCheckSnapshotsGoal() throws Exception {
		
		ClassLoader classLoader = getClass().getClassLoader();
		File pom = new File(classLoader.getResource("check-snapshots-test-pom.xml").getFile());
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		CheckSnapshotMojo checkSnapshotMojo = (CheckSnapshotMojo) this.rule.lookupMojo(GOAL, pom);
		Assert.assertNotNull(checkSnapshotMojo);

	}

}
