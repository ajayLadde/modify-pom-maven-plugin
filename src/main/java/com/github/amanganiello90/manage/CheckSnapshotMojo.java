package com.github.amanganiello90.manage;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.amanganiello90.managecore.ReadPoms;

/**
 * check if you have SNAPSHOT dependencies in your maven multi module project.
 * 
 * @author amanganiello90
 * 
 */
@Mojo(requiresProject = true, name = "check-snapshots")
public class CheckSnapshotMojo extends ManageAbstractMojo {

	/**
	 * typology property. The values are: all, dependency or parent.
	 */
	@Parameter(property = "typology", defaultValue = "all")
	private String typology;

	public void execute() throws MojoExecutionException {

		try {

			ReadPoms.checkSnapshots(dir, typology, testPom);

			return;

		} catch (Exception e) {

			throw new MojoExecutionException("Error", e);
		}

	}

}
