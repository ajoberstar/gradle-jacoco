# gradle-jacoco

A plugin for adding [Jacoco](http://www.eclemma.org/jacoco/trunk/index.html) support to the Gradle build tool.

**API Documentation**

* [Groovydoc](http://ajoberstar.org/gradle-jacoco/docs/groovydoc)

---

## Adding the Plugin

Add the following lines to your build to use the gradle-jacoco plugin.

```groovy
buildscript {
	repositories { mavenCentral() }
	dependencies { classpath 'org.ajoberstar:gradle-jacoco:0.2.0' }
}

apply plugin: 'jacoco'
```

## Default Behavior

When the plugin is applied, it will add an extension to the project
providing options for configuring the version of Jacoco to use, as well
as applying the Jacoco agent to tasks.

By default, all `Test` tasks will have Jacoco support turned on.

If you use the `sonar` or `sonar-runner` plugins, the `jacoco` plugin will automatically
set the unit test and integration test report paths for the project. It
will assume that unit test data should come from the `test` task and that
integration test data, if any, will come from an `intTest` or `integTest`
task.

Any tasks that have Jacoco applied will have their own extension that allows
configuration of specific Jacoco options.

## Configuring the Plugin

You can use the `jacoco` [extension](http://ajoberstar.org/gradle-jacoco/docs/groovydoc/org/ajoberstar/gradle/jacoco/plugins/JacocoPluginExtension.html)
on the project to do high-level configuration and apply Jacoco to additional tasks.

```groovy
jacoco {
	// change the version of Jacoco in use
	toolVersion = '0.6.2.201302030002'

	// apply to a specific task that implements JavaForkOptions
	applyTo(someJavaExecTask)

	// apply to any collection of tasks. any that implement
	// JavaForkOptions will have Jacoco applied
	applyTo(tasks.withType(JavaExec))
}
```

## Configuring Tasks Using Jacoco

Any tasks that has Jacoco applied to it will have a `jacoco` [extension](http://ajoberstar.org/gradle-jacoco/docs/groovydoc/org/ajoberstar/gradle/jacoco/tasks/JacocoTaskExtension.html)
that provides access to all of the properties passed into the agent. The extension can
also be used to disable Jacoco on the specific task.

```groovy
test {
	jacoco {
		// would have been enabled by default
		enabled = true

		// change path of execution data
		destPath = 'build/somewhere/else.exec'
	}
}
```
## Merging Execution Data

If you need to have a single execution file (which is not required for report generation) you
can use the [JacocoMerge](http://ajoberstar.org/gradle-jacoco/docs/groovydoc/org/ajoberstar/gradle/jacoco/tasks/JacocoMerge.html)
task.

```groovy
import org.ajoberstar.gradle.jacoco.tasks.*

task mergedData(type: JacocoMerge) {
	// can pass in any task set up for Jacoco
	executionData test, integTest
}
```

## Creating HTML Reports

If you want an HTML report of your coverage, you can use the [JacocoReport](http://ajoberstar.org/gradle-jacoco/docs/groovydoc/org/ajoberstar/gradle/jacoco/tasks/JacocoReport.html) task. Report tasks will be created
automatically for `test`, `intTest`, and `integTest` tasks, if any.

```groovy
import org.ajoberstar.gradle.jacoco.tasks.*

task jacocoReport(type: JacocoReport) {
	// can include one or more execution files
	exectionData test, integTest

	// specify one or more source sets that you want to report on the coverage of
	sourceSets project.sourceSets.main

	// can also specify additional class and source dirs
	// additionalClassDirs moreStuff
	// additionalSourceDirs moreSourceStuff
}
```

## Release Notes

**v0.2.0**

- Improved the API of the `JacocoReport` and `JacocoMerge` tasks.
- Added default `JacocoReport` tasks for any `test`, `intTest`, and `integTest` tasks to determine coverage of the `main` source set.
- Support for the `sonar-runner` plugin coming in Gradle 1.5.
- Licensed under Apache Software License.

**v0.1.0**

Initial release. Very rough support in place.
