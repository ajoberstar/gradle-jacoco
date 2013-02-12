package org.ajoberstar.gradle.jacoco.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JacocoPlugin implements Plugin<Project> {
	static final String AGENT_CONFIGURATION_NAME = 'jacocoAgent'
	static final String PLUGIN_EXTENSION_NAME = 'jacoco'

	void apply(Project project) {
		project.plugins.apply(JavaBasePlugin)
		configureJacocoConfigurations(project)
		JacocoPluginExtension extension = project.extensions.create(PLUGIN_EXTENSION_NAME, JacocoPluginExtension, project.configurations[AGENT_CONFIGURATION_NAME])
		configureDefaultDependencies(project, extension)
		applyToDefaultTasks(project, extension)
	}

	private JacocoPluginExtension configureJacocoConfigurations(Project project) {
		project.configurations.add(AGENT_CONFIGURATION_NAME).with {
			visible = false
			transitive = true
			description = 'The Jacoco agent to use to get coverage data.'
			it
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
