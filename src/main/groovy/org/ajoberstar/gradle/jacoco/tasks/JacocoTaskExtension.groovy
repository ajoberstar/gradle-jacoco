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

import org.gradle.api.Project
import org.ajoberstar.gradle.jacoco.JacocoAgentJar

/**
 * Extension for tasks that should run with a Jacoco agent
 * to generate coverage execution data.
 */
class JacocoTaskExtension {
	private final Project project
	private final JacocoAgentJar agent

	/**
	 * Whether or not the task should generate execution data.
	 * Defaults to {@code true}.
	 */
	boolean enabled = true

	/**
	 * The path for the execution data to be written to.
	 */
	Object destPath

	/**
	 * Whether or not data should be appended if the {@code destFile}
	 * already exists. Defaults to {@code true}.
	 */
	boolean append = true

	/**
	 * List of class names that should be included in analysis. Names
	 * can use wildcards (* and ?). If left empty, all classes will
	 * be included. Defaults to an empty list.
	 */
	List includes = []

	/**
	 * List of class names that should be excluded from analysis. Names
	 * can use wildcard (* and ?). Defaults to an empty list.
	 */
	List excludes = []

	/**
	 * List of classloader names that should be excluded from analysis. Names
	 * can use wildcards (* and ?). Defaults to an empty list.
	 */
	List excludeClassLoaders = []

	/**
	 * An identifier for the session written to the execution data. Defaults
	 * to an auto-generated identifier.
	 */
	String sessionId

	/**
	 * Whether or not to dump the coverage data at VM shutdown. Defaults to {@code true}.
	 */
	boolean dumpOnExit = true

	/**
	 * THe type of output to generate. Defaults to {@link Output#FILE}.
	 */
	Output output = Output.FILE

	/**
	 * IP address or hostname to use with {@link Output#TCP_SERVER} or
	 * {@link Output#TCP_CLIENT}. Defaults to localhost.
	 */
	String address

	/**
	 * Port to bind to for {@link Output#TCP_SERVER} or {@link Output#TCP_CLIENT}.
	 * Defaults to 6300.
	 */
	int port

	/**
	 * Path to dump all class files the agent sees are dumped to. Defaults to no dumps.
	 */
	Object classDumpPath

	/**
	 * Whether or not to expose functionality via JMX under {@code org.jacoco:type=Runtime}.
	 * Defaults to {@code false}.
	 */
	boolean jmx = false

	/**
	 * Creates a Jacoco task extension.
	 * @param project the project the task is attached to
	 * @param agent the agent JAR to use for analysis
	 */
	JacocoTaskExtension(Project project, JacocoAgentJar agent) {
		this.project = project
		this.agent = agent
	}

	/**
	 * The path for the execution data to be written to.
	 * @return the destination file
	 */
	File getDestFile() {
		destPath?.with { project.file(it) }
	}

	/**
	 * Directory to dump all class files the agent sees are dumped to. Defaults to no dumps.
	 * @return the class dump directory
	 */
	File getClassDumpDir() {
		classDumpPath?.with { project.file(it) }
	}

	/**
	 * Gets all properties in the format expected of the agent JVM argument.
	 * @return state of extension in a JVM argument
	 */
	String getAsJvmArg() {
		StringBuilder builder = new StringBuilder()
		boolean anyArgs = false
		Closure arg = { name, value ->
			if (value instanceof Boolean || value) {
				if (anyArgs) builder << ','
				builder << name
				builder << '='
				if (value instanceof Collection) {
					builder << value.join(':')
				} else if (value instanceof File) {
					builder << value.canonicalPath
				} else {
					builder << value
				}
				anyArgs = true
			}
		}

		builder << '-javaagent:'
		builder << agent.jar.canonicalPath
		builder << '='
		arg 'destfile', getDestFile()
		arg 'append', getAppend()
		arg 'includes', getIncludes()
		arg 'excludes', getExcludes()
		arg 'exclclassloader', getExcludeClassLoaders()
		arg 'sessionid', getSessionId()
		arg 'dumponexit', getDumpOnExit()
		arg 'output', getOutput().asArg
		arg 'address', getAddress()
		arg 'port', getPort()
		arg 'classdumpdir', getClassDumpDir()
		arg 'jmx', getJmx()

		return builder.toString()
	}

	/**
	 * The types of output that the agent
	 * can use for execution data.
	 */
	enum Output {
		FILE,
		TCP_SERVER,
		TCP_CLIENT,
		NONE

		/**
		 * Gets type in format of agent argument.
		 */
		String getAsArg() {
			return toString().toLowerCase().replaceAll('_', '')
		}
	}
}
