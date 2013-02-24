package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JacocoReport extends JacocoBase {
	@InputFiles
	FileCollection executionData

	@InputFiles
	FileCollection classFiles

	@InputFiles
	FileCollection sourceFiles

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

	@OutputDirectory
	File getDestDir() {
		return getProject().file(destPath)
	}
}
