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
