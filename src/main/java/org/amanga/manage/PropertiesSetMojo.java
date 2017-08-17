package org.amanga.manage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.amanga.managecore.PropertiesUtil;

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
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

/**
 * Set parent pom and eventual properties to specified value from a properties
 * file
 * 
 * @author amanganiello90
 * 
 */
@Mojo(requiresProject = true, name = "properties-set")
public class PropertiesSetMojo extends ManageAbstractMojo {

	public void execute() throws MojoExecutionException {

		try {
			
			this.initialize();

			Map<String, String> parentPropValues = new HashMap<String, String>();

			// get all properties from file
			Properties prop = PropertiesUtil.getPropertiesFile(dir + "/set.properties");

			// get eventual parent property from file
			PropertiesUtil.getParentProperty(prop, parentPropValues);
			String parentArtifactProp = PropertiesUtil.getArtifactParentProperty(parentPropValues);
			String parentVersionProp = PropertiesUtil.getVersionParentProperty(parentPropValues);

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

			// update properties

			PropertiesUtil.setPropertiesOnPom(dir, prop, parentArtifactProp, parentVersionProp);

			return;

		} catch (Exception e) {

			throw new MojoExecutionException("Error", e);
		}

	}

}
