package org.ajoberstar.gradle.jacoco.plugins

import groovy.util.logging.Slf4j
import org.gradle.api.Project
import org.gradle.process.JavaForkOptions
import org.gradle.api.tasks.TaskCollection
import org.ajoberstar.gradle.jacoco.JacocoAgentJar
import org.ajoberstar.gradle.jacoco.tasks.JacocoTaskExtension

@Slf4j
class JacocoPluginExtension {
	static final String TASK_EXTENSION_NAME = 'jacoco'
	String toolVersion = '0.6.1.201212231917'

	protected final Project project
	private final JacocoAgentJar agent

	JacocoPluginExtension(Project project, JacocoAgentJar agent) {
		this.project = project
		this.agent = agent
	}

	void applyTo(JavaForkOptions task) {
		log.debug "Applying Jacoco to $task.name"
		JacocoTaskExtension extension = task.extensions.create(TASK_EXTENSION_NAME, JacocoTaskExtension, project, agent)
		task.jacoco.destPath = { "${project.buildDir}/jacoco/${task.name}.exec" }
		task.doFirst {
			//add agent
			if (extension.enabled) {
				task.jvmArgs extension.asJvmArg
			}
		}
	}

	void applyTo(TaskCollection tasks) {
		tasks.withType(JavaForkOptions) {
			applyTo(it)
		}
	}
}
