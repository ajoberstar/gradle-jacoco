package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles

/**
 * Base class for Jacoco tasks.
 */
abstract class JacocoBase extends DefaultTask {
	/**
	 * Classpath containing Jacoco classes for use by the task.
	 */
	@InputFiles
	FileCollection jacocoClasspath
}
