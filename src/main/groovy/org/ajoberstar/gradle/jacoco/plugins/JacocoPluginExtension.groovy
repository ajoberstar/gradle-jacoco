package org.ajoberstar.gradle.jacoco.plugins

import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskCollection
import org.ajoberstar.gradle.jacoco.tasks.JacocoTaskExtension

class JacocoPluginExtension {
	static final String TASK_EXTENSION_NAME = 'jacoco'
	String toolVersion = '0.6.1.201212231917'

	private final Configuration agentConf

	JacocoPluginExtension(Configuration agentConf) {
		this.agentConf = agentConf
	}

	void applyTo(JavaExec task) {
		JacocoTaskExtension extension = task.extensions.create(TASK_EXTENSION_NAME, JacocoTaskExtension, agentConf)
		task.jacoco.destPath = { "${task.project.buildDir}/jacoco/${task.name}.exec" }
		task.doFirst {
			//add agent
			if (extension.enabled) {
				task.jvmArgs extension.asJvmArg
			}
		}
	}

	void applyTo(TaskCollection tasks) {
		tasks.withType(JavaExec) {
			applyTo(it)
		}
	}
}
