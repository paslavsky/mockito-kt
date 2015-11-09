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

import org.mockito.Mockito
import kotlin.reflect.KClass

/**
 * In common this library is facade around [Mockito library](http://mockito.org/) for [Kotlin](www.kotlinlang.org)
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */

/**
 * ## Example 1
 *
 * ```
 * val list = mock(MutableList::class)
 *
 * list.add("String 1")
 * list.add("String 2")
 *
 * verify(list) {
 *     times(2).add(anyString())
 * }
 * ```
 *
 * ## Example 2
 * ```
 * val list = mock(MutableList::class) {
 *     `when`(mock.size()).thenReturn(5)
 *     `when`(mock.get(eq(3))).thenReturn("String 4")
 * }
 * ...
 * ```
 */
public fun <T : Any> mock(kClass: KClass<T>, setup: Mock<T>.() -> Unit = {}): T {
    val mock = Mockito.mock(kClass.java)
    val mockKt = Mock<T>(mock)
    mockKt.setup()
    mockKt.actionChains.asSequence().forEach { it.done() }
    return mock
}

public fun <T : Any> spy(obj: T, setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(obj)
    val mockKt = Mock<T>(spy)
    mockKt.setup()
    mockKt.actionChains.asSequence().forEach { it.done() }
    return spy
}

public fun <T : Any> spy(classToSpy: KClass<T>, setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(classToSpy.java)
    val mockKt = Mock<T>(spy)
    mockKt.setup()
    mockKt.actionChains.asSequence().forEach { it.done() }
    return spy
}

public fun <T : Any> verify(mock: T, verify: Verification<T>.() -> Unit) {
    Verification(mock).verify()
}

public fun verifyNoMoreInteractions(vararg mocks: Any) = Mockito.verifyNoMoreInteractions(*mocks)
public fun verifyZeroInteractions(vararg mocks: Any) = Mockito.verifyZeroInteractions(*mocks)