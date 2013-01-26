package org.ajoberstar.gradle.jacoco.util

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
