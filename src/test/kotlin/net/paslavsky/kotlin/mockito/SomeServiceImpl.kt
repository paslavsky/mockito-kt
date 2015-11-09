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

/**
 * For testing purpose
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
public open class SomeServiceImpl(public val boolValue: Boolean) : SomeService {
    override fun foo(): String = "Test value"

    override fun bar(dataValue: SomeData): Pair<Int, Int> = dataValue.intValue to dataValue.intValue

    override fun withNulls(dataValue: SomeData?): String? = dataValue?.toString()
}