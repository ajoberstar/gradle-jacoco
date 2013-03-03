/*
 * Copyright 2013 the original author or authors.
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
