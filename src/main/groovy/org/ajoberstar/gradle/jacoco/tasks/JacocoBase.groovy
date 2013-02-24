package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles

abstract class JacocoBase extends DefaultTask {
	@InputFiles
	FileCollection jacocoClasspath
}
