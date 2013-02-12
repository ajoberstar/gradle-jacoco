package org.ajoberstar.gradle.jacoco.tasks

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class JacocoTaskExtension {
	private final Project project
	private final FileCollection agentConf

	boolean enabled = true

	Object destPath
	boolean append = true
	List includes = []
	List excludes = []
	List excludeClassLoaders = []
	String sessionId
	boolean dumpOnExit = true
	Output output = Output.FILE
	String address
	int port
	Object classDumpPath
	boolean jmx = false

	JacocoTaskExtension(Project project, FileCollection agentConf) {
		this.project = project
		this.agentConf = agentConf
	}

	File getDestFile() {
		destPath?.with { project.file(it) }
	}

	File getClassDumpDir() {
		classDumpPath?.with { project.file(it) }
	}

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
		builder << agentConf.singleFile.canonicalPath
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

	enum Output {
		FILE,
		TCP_SERVER,
		TCP_CLIENT,
		NONE

		String getAsArg() {
			return toString().toLowerCase().replaceAll('_', '')
		}
	}
}
