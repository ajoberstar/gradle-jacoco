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
