package org.ajoberstar.gradle.jacoco.tasks

import spock.lang.Specification
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.ajoberstar.gradle.jacoco.JacocoAgentJar

class JacocoTaskExtensionSpec extends Specification {
	Project project = Mock()
	JacocoAgentJar agent = Mock()
	JacocoTaskExtension extension = new JacocoTaskExtension(project, agent)

	def setup() {
		project.file(_) >> { println it; new File(it[0]) }
	}

	def 'asJvmArg with default arguments assembles correct string'() {
		given:
		agent.jar >> new File('fakeagent.jar')
		expect:
		extension.asJvmArg == "-javaagent:${fullPath('fakeagent.jar')}=append=true,dumponexit=true,output=file,jmx=false"
	}

	def 'asJvmArg with all arguments assembles correct string'() {
		given:
		agent.jar >> new File('fakeagent.jar')
		extension.with {
			destPath = 'build/jacoco/fake.exec'
			append = false
			includes = ['org.*', '*.?acoco*']
			excludes = ['org.?joberstar']
			excludeClassLoaders = ['com.sun.*', 'org.fak?.*']
			sessionId = 'testSession'
			dumpOnExit = false
			output = JacocoTaskExtension.Output.TCP_SERVER
			address = '1.1.1.1'
			port = 100
			classDumpPath = 'build/jacoco-dump'
			jmx = true
		}

		def expected = new StringBuilder().with { builder ->
			builder << "-javaagent:${fullPath('fakeagent.jar')}="
			builder << "destfile=${fullPath('build/jacoco/fake.exec')},"
			builder << "append=false,"
			builder << "includes=org.*:*.?acoco*,"
			builder << "excludes=org.?joberstar,"
			builder << "exclclassloader=com.sun.*:org.fak?.*,"
			builder << "sessionid=testSession,"
			builder << "dumponexit=false,"
			builder << "output=tcpserver,"
			builder << "address=1.1.1.1,"
			builder << "port=100,"
			builder << "classdumpdir=${fullPath('build/jacoco-dump')},"
			builder << "jmx=true"
			builder.toString()
		}
		expect:
		extension.asJvmArg == expected
	}

	def 'asJvmArg fails if agent cannot extract the JAR'() {
		given:
		agent.jar >> { throw new Exception() }
		when:
		extension.asJvmArg
		then:
		thrown Exception
	}

	private String fullPath(String relativePath) {
		return new File(relativePath).canonicalPath
	}
}
