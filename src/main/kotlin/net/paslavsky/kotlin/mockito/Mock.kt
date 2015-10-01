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
import org.mockito.stubbing.Answer
import org.mockito.stubbing.Stubber
import kotlin.reflect.KClass

/**
 * This is wrapper around mock object that used to pull-in to setup method and provides matcher's
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
public class Mock<M : Any>(public val mock: M) : MatchersKt() {
    public fun <T> `when`(metthodCall: T) = Mockito.`when`(metthodCall)
    public fun doThrow(toBeThrown: Throwable): Stubber = Mockito.doThrow(toBeThrown)
    public fun doThrow(toBeThrown: KClass<out Throwable>): Stubber = Mockito.doThrow(toBeThrown.java)
    public fun doCallRealMethod(): Stubber = Mockito.doCallRealMethod()
    public fun <T> doAnswer(answer: (invocation: InvocationOnMock) -> T): Stubber = Mockito.doAnswer(answer)
    public fun doNothing(): Stubber = Mockito.doNothing()
    public fun <T : Any> doReturn(toBeReturned: T?): Stubber = Mockito.doReturn(toBeReturned)
}