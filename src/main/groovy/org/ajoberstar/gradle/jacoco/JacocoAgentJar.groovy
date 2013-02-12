package org.ajoberstar.gradle.jacoco

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class JacocoAgentJar {
	private final FileCollection agentConf
	private final Project project
	private File agentJar

	JacocoAgentJar(Project project, FileCollection agentConf) {
		this.project = project
		this.agentConf = agentConf
	}

	File getJar() {
		if (!agentJar) {
			agentJar = project.zipTree(agentConf.singleFile).filter { it.name == 'jacocoagent.jar' }.singleFile
		}
		return agentJar
	}
}
