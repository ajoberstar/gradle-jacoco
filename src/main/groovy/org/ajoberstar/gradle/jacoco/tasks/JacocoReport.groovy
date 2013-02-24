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
