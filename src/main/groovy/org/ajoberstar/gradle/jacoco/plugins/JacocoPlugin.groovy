package org.ajoberstar.gradle.jacoco.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.ajoberstar.gradle.jacoco.JacocoAgentJar

class JacocoPlugin implements Plugin<Project> {
	static final String AGENT_CONFIGURATION_NAME = 'jacocoAgent'
	static final String PLUGIN_EXTENSION_NAME = 'jacoco'

	void apply(Project project) {
		configureJacocoConfigurations(project)
		JacocoAgentJar agent = new JacocoAgentJar(project, project.configurations[AGENT_CONFIGURATION_NAME])
		JacocoPluginExtension extension = project.extensions.create(PLUGIN_EXTENSION_NAME, JacocoPluginExtension, project, agent)
		configureDefaultDependencies(project, extension)
		applyToDefaultTasks(project, extension)
	}

	private void configureJacocoConfigurations(Project project) {
		project.configurations.add(AGENT_CONFIGURATION_NAME).with {
			visible = false
			transitive = true
			description = 'The Jacoco agent to use to get coverage data.'
		}
	}

	private void configureDefaultDependencies(Project project, JacocoPluginExtension extension) {
		project.dependencies {
			jacocoAgent "org.jacoco:org.jacoco.agent:${extension.toolVersion}"
		}
	}

	private void applyToDefaultTasks(Project project, JacocoPluginExtension extension) {
		extension.applyTo(project.tasks.withType(Test))
	}
}
