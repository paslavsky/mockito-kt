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

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFails

/**
 * Tests for **Mockito Kt** library
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
class MockTest : Spek() {
    init {
        given("some service class") {
            val serviceClass = SomeService::class
            on("creating mock that based on this class") {
                val mockService = mock(serviceClass) {
                    whenMock { foo() }.thenReturn("bla bla bla")
                    whenMock { bar(eq(SomeData("abc", 123))) }.thenReturn(123 to 321)
                }
                it("should work as expected") {
                    assertEquals("bla bla bla", mockService.foo())
                    assertEquals(123 to 321, mockService.bar(SomeData("abc", 123)))
                }
            }

            on("creating mock with any matching of the unregistered type") {
                it("should fails") {
                    assertFails {
                        mock(serviceClass) {
                            whenMock { bar(any(SomeData::class)) }.thenReturn(123 to 321)
                        }
                    }
                }
            }

            on("creating mock with any matching and default value") {
                val mockService = mock(serviceClass) {
                    defaults.register(SomeData::class to SomeData("123", 321))
                    whenMock { bar(any(SomeData::class)) }.thenReturn(123 to 321)
                }
                it("should work as expected") {
                    assertEquals(123 to 321, mockService.bar(SomeData("abc", 123)))
                }
            }

            on("creating mock with multiple actions") {
                val mockService = mock(serviceClass) {
                    whenMock { foo() }.thenReturn("one").thenReturn("two").thenThrow(IllegalStateException::class)
                }
                it("should return `one`") {
                    assertEquals("one", mockService.foo())
                }
                it("should return `two`") {
                    assertEquals("two", mockService.foo())
                }
                it("should throw exception") {
                    assertFails {
                        mockService.foo()
                    }
                }
            }

            on("mocking method that can accept nulls") {
                val mockService = mock(serviceClass) {
                    whenMock {
                        withNulls(anyNullable())
                    }.thenReturn(null)
                }

                it("should work with null value as an argument") {
                    assertEquals(null, mockService.withNulls(null))
                }
            }

            on("call few methods") {
                val mockService = mock(serviceClass)
                mockService.foo()
                mockService.bar(SomeData("Test", 1))
                it("should able check this invocation") {
                    once(mockService) { match ->
                        foo()
                        bar(match.eq(SomeData("Test", 1)))
                    }
                }
            }
        }
    }
}