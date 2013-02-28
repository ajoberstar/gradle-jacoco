/* Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.ajoberstar.gradle.jacoco

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

/**
 * Helper to resolve the {@code jacocoagent.jar} from inside
 * of the {@code org.jacoco.agent.jar}.
 */
class JacocoAgentJar {
	private final FileCollection agentConf
	private final Project project
	private File agentJar

	/**
	 * Constructs a new agent JAR wrapper.
	 * @param project a project that can be used to resolve files
	 * @param agentConf the configuration that the agent JAR is located in
	 */
	JacocoAgentJar(Project project, FileCollection agentConf) {
		this.project = project
		this.agentConf = agentConf
	}

	/**
	 * Unzips the resolved {@code org.jacoco.agent.jar} to retrieve
	 * the {@code jacocoagent.jar}.
	 * @return a file pointing to the {@code jacocoagent.jar}
	 */
	File getJar() {
		if (!agentJar) {
			agentJar = project.zipTree(agentConf.singleFile).filter { it.name == 'jacocoagent.jar' }.singleFile
		}
		return agentJar
	}
}
