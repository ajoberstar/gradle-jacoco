package org.ajoberstar.gradle.jacoco.plugins

import org.gradle.api.Task

import org.ajoberstar.gradle.jacoco.tasks.JacocoTaskExtension

class JacocoPluginExtension {
	static final String TASK_EXTENSION_NAME = 'jacoco'
	String toolVersion = '0.6.1.201212231917'

	void applyTo(JavaExec task) {
		JacocoTaskExtension extension = task.extensions.create(TASK_EXTENSION_NAME, JacocoTaskExtension)
		task.doFirst {
			//add agent
			if (extension.enabled) {
				task.jvmArgs extension.asJvmArg
			}
		}

		task.doLast {
			//generate reports
		}
	}

	void applyTo(TaskCollection tasks) {
		tasks.withType(JavaExec) {
			applyTo(it)
		}
	}
}
