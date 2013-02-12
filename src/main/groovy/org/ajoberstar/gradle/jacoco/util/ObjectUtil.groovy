package org.ajoberstar.gradle.jacoco.util

import java.util.concurrent.Callable

class ObjectUtil {
	static Object unpack(Object obj) {
		if (obj instanceof Callable) {
			return obj.call()
		} else if (obj instanceof Closure) {
			return obj.call()
		} else {
			return obj
		}
	}

	static String unpackString(Object obj) {
		return unpack(obj).toString()
	}
}
