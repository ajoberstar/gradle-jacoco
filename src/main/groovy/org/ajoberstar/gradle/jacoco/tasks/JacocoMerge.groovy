package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class JacocoMerge extends JacocoBase {
	@InputFiles
	FileCollection executionData

	Object destPath = "${getProject().getBuildDir()}/jacoco/${getName()}.exec"

	@TaskAction
	void merge() {
		getAnt().taskdef(name:'merge', classname:'org.jacoco.ant.MergeTask', classpath:getJacocoClasspath().asPath)
		getAnt().merge(destfile:getDestFile()) {
			getExecutionData().addToAntBuilder(ant, 'resources')
		}
	}

	@OutputFile
	File getDestFile() {
		return getProject().file(destPath)
	}
}
