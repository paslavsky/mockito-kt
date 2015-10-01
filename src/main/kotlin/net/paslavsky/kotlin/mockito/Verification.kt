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

/**
 * This is wrapper around mock object to provide versification API
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
public class Verification<M : Any>(private val mock: M) : MatchersKt() {
    public fun onece(): M = Mockito.verify(mock, Mockito.times(1))
    public fun never(): M = Mockito.verify(mock, Mockito.never())
    public fun times(wantedNumberOfInvocations: Int): M = Mockito.verify(mock, Mockito.times(wantedNumberOfInvocations))
    public fun atLeastOnce(): M = Mockito.verify(mock, Mockito.atLeastOnce())
    public fun atLeast(minNumberOfInvocations: Int): M = Mockito.verify(mock, Mockito.atLeast(minNumberOfInvocations))
    public fun atMost(maxNumberOfInvocations: Int): M = Mockito.verify(mock, Mockito.atMost(maxNumberOfInvocations))
    public fun calls(wantedNumberOfInvocations: Int): M = Mockito.verify(mock, Mockito.calls(wantedNumberOfInvocations))
    public fun only(): M = Mockito.verify(mock, Mockito.only())
    public fun timeout(millis: Long): M = Mockito.verify(mock, Mockito.timeout(millis))
    public fun after(millis: Int): M = Mockito.verify(mock, Mockito.after(millis))
}