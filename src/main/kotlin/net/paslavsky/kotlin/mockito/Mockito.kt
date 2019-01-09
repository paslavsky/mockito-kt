@file:Suppress("unused")

/*
 * Copyright (c) 2019 Andrey Paslavsky.
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

/**
 * In common this library is facade around [Mockito library](http://mockito.org/) for [Kotlin](www.kotlinlang.org)
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 */
package net.paslavsky.kotlin.mockito

import org.mockito.Mockito
import kotlin.reflect.KClass

/**
 * ## Example 1
 *
 * ```
 * val list = mock<MutableList<String>>()
 *
 * list.add("String 1")
 * list.add("String 2")
 *
 * list.verify {
 *     times(2).add(any())
 * }
 * ```
 *
 * ## Example 2
 * ```
 * val list = MutableList::class.mock {
 *     on { size }.thenReturn(5)
 *     on { this[eq(3)] }.thenReturn("String 4")
 * }
 * ...
 * ```
 */
fun <T : Any> KClass<T>.mock(setup: Mock<T>.() -> Unit = {}): T {
    val mock = Mockito.mock(java)
    val mockKt = Mock<T>(mock)
    mockKt.setup()
    mockKt.actionChains.forEach { it.done() }
    return mock
}

inline fun <reified T : Any> mock(noinline setup: Mock<T>.() -> Unit = {}) = T::class.mock(setup)

fun <T : Any> T.spy(setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(this)
    val mockKt = Mock<T>(spy)
    mockKt.setup()
    mockKt.actionChains.forEach { it.done() }
    return spy
}

fun <T : Any> KClass<T>.spy(setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(java)
    val mockKt = Mock<T>(spy)
    mockKt.setup()
    mockKt.actionChains.forEach { it.done() }
    return spy
}

inline fun <reified T : Any> spy(noinline setup: Mock<T>.() -> Unit = {}) = T::class.spy(setup)

fun <T : Any> T.verify(verify: Verification<T>.() -> Unit) {
    Verification(this).verify()
}

fun verifyNoMoreInteractions(vararg mocks: Any) = Mockito.verifyNoMoreInteractions(*mocks)
fun verifyZeroInteractions(vararg mocks: Any) = Mockito.verifyZeroInteractions(*mocks)

fun <T : Any> T.verifyOnce(checks: T.(match: MatchersKt) -> Unit) {
    Mockito.verify(this).checks(Matchers)
}