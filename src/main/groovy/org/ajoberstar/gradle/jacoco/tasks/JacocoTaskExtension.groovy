package org.ajoberstar.gradle.jacoco.tasks

class JacocoTaskExtension {
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

	File getDestFile() {
		return null
	}

	File getClassDumpDir() {
		return null
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
				} else {
					builder << value
				}
				anyArgs = true
			}
		}

		builder << '-javaagent:'
		//builder << agentJarPath
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
