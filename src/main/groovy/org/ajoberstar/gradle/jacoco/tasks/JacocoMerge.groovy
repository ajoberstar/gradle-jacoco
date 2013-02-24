package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Task to merge multiple execution data files into one.
 */
class JacocoMerge extends JacocoBase {
	/**
	 * Collection of execution data files to merge.
	 */
	@InputFiles
	FileCollection executionData

	/**
	 * Path to write merged execution data to. Defaults to {@code build/jacoco/<task name>.exec}
	 */
	Object destPath = "${getProject().getBuildDir()}/jacoco/${getName()}.exec"

	@TaskAction
	void merge() {
		getAnt().taskdef(name:'merge', classname:'org.jacoco.ant.MergeTask', classpath:getJacocoClasspath().asPath)
		getAnt().merge(destfile:getDestFile()) {
			getExecutionData().addToAntBuilder(ant, 'resources')
		}
	}

	/**
	 * Path to write merged execution daat to.
	 */
	@OutputFile
	File getDestFile() {
		return getProject().file(destPath)
	}
}
