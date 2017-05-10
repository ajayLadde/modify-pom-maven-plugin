package org.amanga.manage;

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

/**
 * Set parent pom and eventual properties to specified value from a properties
 * file
 * 
 * @author amanganiello
 * 
 */
@Mojo(requiresProject = false, name = "properties-set")
public class PropertiesSetMojo extends ManageAbstractMojo {

	/**
	 * property related what branch to process
	 */
	@Parameter(property = "branch")
	private String gitBranch;

	public void execute() throws MojoExecutionException {

		return;

	}

}
