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
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Stubber
import kotlin.reflect.KClass

/**
 * Stubber for Mockito Kt
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
@Suppress("unused")
class ThenAction<T, M : Any>(
        private val mock: M,
        private val call: M.() -> T,
        private val chains: MutableSet<ThenAction<*,*>.ActionChain<*>>
) {
    private val dummy: Dummy = Mockito.mock(Dummy::class.java)

    private open class Dummy {
        open fun doSomething(): Any? = null
    }

    fun thenThrow(toBeThrown: KClass<out Throwable>) = ActionChain<T>({ Mockito.doThrow(toBeThrown.java) })
    fun thenThrow(toBeThrown: Throwable) = ActionChain<T>({ Mockito.doThrow(toBeThrown) })
    fun thenCallRealMethod() = ActionChain<T>({ Mockito.doCallRealMethod() })
    fun thenAnswer(answer: (invocation: InvocationOnMock) -> T) = ActionChain<T>({ Mockito.doAnswer(answer) })
    fun thenNothing() = ActionChain<T>({ Mockito.doNothing() })
    fun thenReturn(toBeReturned: T?) = ActionChain<T>({ Mockito.doReturn(toBeReturned) })

    private class PreviousActionHolder(internal val acton: () -> Stubber)

    inner class ActionChain<T>(private var acton: () -> Stubber) {
        init {
            chains.add(this)
        }

        fun thenThrow(toBeThrown: KClass<out Throwable>) = addAction { doThrow(toBeThrown.java) }
        fun thenThrow(toBeThrown: Throwable) = addAction { doThrow(toBeThrown) }
        fun thenCallRealMethod() = addAction { doCallRealMethod() }
        fun thenAnswer(answer: (invocation: InvocationOnMock) -> T) = addAction { doAnswer(answer) }
        fun thenNothing() = addAction { doNothing() }
        fun thenReturn(toBeReturned: T?) = addAction { doReturn(toBeReturned) }

        private fun addAction(newAction: Stubber.() -> Stubber): ActionChain<T> {
            val previousActionHolder = PreviousActionHolder(acton)
            acton = { previousActionHolder.acton().newAction() }
            return this
        }

        internal fun done() {
            try {
                acton().`when`(mock).call()
            } catch(e: Exception) {
                resetMockitoStubbingState()
                throw e;
            }
        }

        private fun resetMockitoStubbingState() {
            try {
                Mockito.doReturn(Any()).`when`(dummy).doSomething()
            } catch (ignore: Exception) {
            } finally {
                Mockito.reset(dummy)
            }
        }
    }
}