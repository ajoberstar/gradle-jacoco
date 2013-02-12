package org.ajoberstar.gradle.jacoco.plugins

import spock.lang.Specification
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.Test
import org.gradle.testfixtures.ProjectBuilder
import org.ajoberstar.gradle.jacoco.tasks.JacocoTaskExtension

class JacocoPluginSpec extends Specification {
	Project project = ProjectBuilder.builder().withProjectDir(new File(System.properties['java.io.tmpdir'])).build()

	def setup() {
		project.apply plugin: 'jacoco'
	}

	def 'jacoco applied to specific JavaExec task'() {
		given:
		JavaExec task = project.tasks.add('exec', JavaExec)
		when:
		project.jacoco.applyTo(task)
		then:
		task.extensions.getByType(JacocoTaskExtension) != null
	}

	def 'jacoco applied to Test task'() {
		given:
		Test task = project.tasks.add('test', Test)
		expect:
		task.extensions.getByType(JacocoTaskExtension) != null
	}
}
