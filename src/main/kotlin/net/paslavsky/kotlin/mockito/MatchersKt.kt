/*
 * Copyright (c) 2015 Andrey Paslavsky.
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
 *
 */

package net.paslavsky.kotlin.mockito

import org.mockito.Matchers
import kotlin.reflect.KClass
import org.hamcrest.Matcher

/**
 * # Matching API
 *
 * Some methods of this class is simple `delegates` to the [Matchers] methods. Another part of the methods
 * trying to pick up `not null` default return values to avoid exceptions. For this used [Defaults] property.
 *
 * if your tests failed with message:
 * ```
 * Caused by: java.lang.IllegalStateException: Please register default value for your_class_name
 * ```
 *
 * then please register your default value like in example below:
 *
 * ```
 * mock(Foo.class) {
 *     defaults.register(Bar::class to barValue)
 *     ...
 * }
 * ```
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
@Suppress("UNCHECKED_CAST", "unused")
abstract class MatchersKt {
    val defaults = Defaults()

    fun <T : Any> any(kClass: KClass<T>): T {
        Matchers.any(kClass.java)
        return defaults.valueFor(kClass)
    }

    fun <T> anyVararg(): Array<T> {
        Matchers.anyVararg<T>()
        @Suppress("CAST_NEVER_SUCCEEDS")
        return defaults.valueFor(Array<Any>::class) as Array<T>
    }

    fun any(): Any = any(Any::class)
    fun <T : Any> anyNullable(): T? = Matchers.any<T>()
    fun anyBoolean(): Boolean = any(Boolean::class)
    fun anyByte(): Byte = any(Byte::class)
    fun anyChar(): Char = any(Char::class)
    fun anyInt(): Int = any(Int::class)
    fun anyLong(): Long = any(Long::class)
    fun anyFloat(): Float = any(Float::class)
    fun anyDouble(): Double = any(Double::class)
    fun anyShort(): Short = any(Short::class)
    fun anyString(): String = any(String::class)
    fun <T : Any> anyList(): List<T> = any(List::class) as List<T>
    fun <T : Any> anySet(): Set<T> = any(Set::class) as Set<T>
    fun <T : Any> anyCollection(): Collection<T> = any(Collection::class) as Collection<T>
    fun <K : Any, V : Any> anyMap(): Map<K, V> = any(Map::class) as Map<K, V>
    fun <T : Any> anyMutableList(): MutableList<T> = any(MutableList::class) as MutableList<T>
    fun <T : Any> anyMutableSet(): MutableSet<T> = any(MutableSet::class) as MutableSet<T>
    fun <T : Any> anyMutableCollection(): MutableCollection<T> = any(MutableCollection::class) as MutableCollection<T>
    fun <K : Any, V : Any> anyMutableMap(): MutableMap<K, V> = any(MutableMap::class) as MutableMap<K, V>

    fun <T : Any> isA(kClass: KClass<T>): T {
        Matchers.isA(kClass.java)
        return defaults.valueFor(kClass)
    }

    fun <T> eq(t: T): T {
        Matchers.eq(t)
        return t
    }

    fun <T> refEq(t: T): T {
        Matchers.refEq(t)
        return t
    }

    fun <T> same(t: T): T {
        Matchers.same(t)
        return t
    }

    fun <T : Any> isNull(): T? {
        Matchers.isNull()
        return null
    }

    fun <T : Any> isNotNull(kClass: KClass<T>): T? = Matchers.isNotNull(kClass.java)
    
    fun contains(substring:String) :String = Matchers.contains(substring)
    fun matches(regex: String): String = Matchers.matches(regex)
    fun endsWith(suffix: String): String = Matchers.endsWith(suffix)
    fun startsWith(prefix: String): String = Matchers.startsWith(prefix)

    fun <T : Any> argThat(matcher: Matcher<T>, kClass: KClass<T>): T {
        Matchers.argThat(matcher)
        return defaults.valueFor(kClass)
    }
}