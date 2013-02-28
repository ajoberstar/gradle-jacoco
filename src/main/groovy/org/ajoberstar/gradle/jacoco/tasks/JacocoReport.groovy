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
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Task to generate HTML reports of Jacoco coverage data.
 */
class JacocoReport extends JacocoBase {
	/**
	 * Collection of execution data files to analyze.
	 */
	@InputFiles
	FileCollection executionData

	/**
	 * Collection of class files to get coverage data for.
	 */
	@InputFiles
	FileCollection classFiles

	/**
	 * Collection of source files for {@link #classFiles}.
	 */
	@InputFiles
	FileCollection sourceFiles

	/**
	 * Path to write report to. Defaults to {@code build/reports/jacoco/<task name>}.
	 */
	Object destPath = "${getProject().getBuildDir()}/reports/jacoco/${getName()}"

	@TaskAction
	void generate() {
		getAnt().taskdef(name:'report', classname:'org.jacoco.ant.ReportTask', classpath:getJacocoClasspath().asPath)
		getAnt().report {
			executionData {
				getExecutionData().addToAntBuilder(getAnt(), 'resources')
			}
			structure(name:getProject().getName()) {
				classfiles {
					getClassFiles().filter { it.exists() }.addToAntBuilder(getAnt(), 'resources')
				}
				sourcefiles {
					getSourceFiles().filter { it.exists() }.addToAntBuilder(getAnt(), 'resources')
				}
			}
			html(destdir:getDestDir())
		}
	}

	/**
	 * Gets the directory to write the report to.
	 */
	@OutputDirectory
	File getDestDir() {
		return getProject().file(destPath)
	}
}
