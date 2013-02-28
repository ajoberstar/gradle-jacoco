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
package org.ajoberstar.gradle.jacoco.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.plugins.sonar.SonarPlugin
import org.ajoberstar.gradle.jacoco.JacocoAgentJar
import org.ajoberstar.gradle.jacoco.tasks.JacocoBase

/**
 * Plugin that provides support for generating Jacoco coverage data.
 */
class JacocoPlugin implements Plugin<Project> {
	static final String AGENT_CONFIGURATION_NAME = 'jacocoAgent'
	static final String ANT_CONFIGURATION_NAME = 'jacocoAnt'
	static final String PLUGIN_EXTENSION_NAME = 'jacoco'

	/**
	 * Applies the plugin to the given project.
	 * @param project the project to apply to
	 */
	void apply(Project project) {
		configureJacocoConfigurations(project)
		JacocoAgentJar agent = new JacocoAgentJar(project, project.configurations[AGENT_CONFIGURATION_NAME])
		JacocoPluginExtension extension = project.extensions.create(PLUGIN_EXTENSION_NAME, JacocoPluginExtension, project, agent)
		configureDefaultDependencies(project, extension)
		configureTaskClasspaths(project)
		applyToDefaultTasks(project, extension)
		configureSonarPlugin(project)
	}

	/**
	 * Creates the configurations used by plugin.
	 * @param project the project to add the configurations to
	 */
	private void configureJacocoConfigurations(Project project) {
		project.configurations.add(AGENT_CONFIGURATION_NAME).with {
			visible = false
			transitive = true
			description = 'The Jacoco agent to use to get coverage data.'
		}
		project.configurations.add(ANT_CONFIGURATION_NAME).with {
			visible = false
			transitive = true
			description = 'The Jacoco ant tasks to use to get execute Gradle tasks.'
		}
	}

	/**
	 * Configures the default dependencies used in the plugin's configurations.
	 * @param project the project to add the dependencies to
	 * @param extension the extension that has the tool version to use
	 */
	private void configureDefaultDependencies(Project project, JacocoPluginExtension extension) {
		project.dependencies {
			jacocoAgent "org.jacoco:org.jacoco.agent:${extension.toolVersion}"
			jacocoAnt "org.jacoco:org.jacoco.ant:${extension.toolVersion}"
		}
	}

	/**
	 * Configures the classpath for Jacoco tasks.
	 * @param project the project to configure tasks for
	 */
	private void configureTaskClasspaths(Project project) {
		project.tasks.withType(JacocoBase) {
			jacocoClasspath = project.configurations[ANT_CONFIGURATION_NAME]
		}
	}

	/**
	 * Applies the Jacoco agent to all tasks of type {@code Test}.
	 * @param project the project with the tasks to configure
	 * @param extension the extension to apply Jacoco with
	 */
	private void applyToDefaultTasks(Project project, JacocoPluginExtension extension) {
		extension.applyTo(project.tasks.withType(Test))
	}

	/**
	 * Configures default paths to Jacoco execution data for unit and
	 * integration tests. Only configures them if tasks of the default
	 * names exist. This is {@code test} for unit tests and either
	 * {@code integTest} or {@code intTest} for integration tests.
	 * @param currentProject the project to configure Sonar for
	 */
	private void configureSonarPlugin(Project currentProject) {
		currentProject.rootProject.plugins.withType(SonarPlugin) {
			currentProject.sonar.project.withProjectProperties { props ->
				if (currentProject.tasks.findByPath('test') instanceof Test) {
					props['sonar.jacoco.reportPath'] = currentProject.tasks.test.jacoco.destFile
				}
				if (currentProject.tasks.findByPath('intTest') instanceof Test) {
					props['sonar.jacoco.reportPath'] = currentProject.tasks.intTest.jacoco.destFile
				}
				if (currentProject.tasks.findByPath('integTest') instanceof Test) {
					props['sonar.jacoco.itReportPath'] = currentProject.tasks.integTest.jacoco.destFile
				}
			}
		}
	}
}
