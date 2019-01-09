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

import java.lang.reflect.Proxy
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass
import kotlin.reflect.jvm.isAccessible

/**
 * This class helps to solve the problem with null default values
 * This class provides API to register local default value and global
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
open class Defaults {
    private val search = ConcurrentLinkedQueue< (Function1<KClass<*>, Any?>)>()

    open fun <T : Any> valueFor(kClass: KClass<T>) : T = internalLookUp(kClass) ?: Global.valueFor(kClass)

    fun register(lookUpValueFor: (KClass<*>) -> Any?) {
        search.add(lookUpValueFor)
    }

    fun register(pair: Pair<KClass<*>, Any>) {
        search.add { if (pair.first == it) pair.second else null }
    }

    internal fun <T : Any> internalLookUp(kClass: KClass<T>): T? = search.asSequence().map {
        it.invoke(kClass)
    }.filterNotNull().filter {
        kClass.java.isInstance(it)
    }.map {
        @Suppress("UNCHECKED_CAST")
        (it as T)
    }.firstOrNull()

    companion object Global : Defaults() {
        init {
            register { kClass ->
                when (kClass) {
                    Unit::class -> Unit
                    Any::class -> Any()
                    Boolean::class -> false
                    Byte::class -> 0.toByte()
                    Short::class -> 0.toShort()
                    Int::class -> 0
                    Long::class -> 0L
                    Float::class -> 0.0F
                    Double::class -> 0.0
                    Char::class -> '0'
                    String::class -> ""
                    BooleanArray::class -> booleanArrayOf()
                    ByteArray::class -> byteArrayOf()
                    ShortArray::class -> shortArrayOf()
                    IntArray::class -> intArrayOf()
                    LongArray::class -> longArrayOf()
                    FloatArray::class -> floatArrayOf()
                    DoubleArray::class -> doubleArrayOf()
                    CharArray::class -> charArrayOf()
                    Array<Any>::class -> emptyArray<Any>()
                    else -> null
                }
            }
            register { kClass ->
                when (kClass) {
                    List::class -> emptyList<Any>()
                    MutableList::class -> emptyList<Any>()
                    Set::class -> emptySet<Any>()
                    MutableSet::class -> emptySet<Any>()
                    Collection::class -> emptyList<Any>()
                    MutableCollection::class -> emptyList<Any>()
                    Map::class -> emptyMap<Any, Any>()
                    MutableMap::class -> emptyMap<Any, Any>()
                    Sequence::class -> emptySequence<Any>()
                    else -> null
                }
            }
            register { kClass ->
                when (kClass) {
                    Locale::class -> Locale.getDefault()
                    else -> null
                }
            }
            register { kClass ->
                @Suppress("SENSELESS_COMPARISON")
                when {
                    kClass == KClass::class -> KClass::class
                    kClass == Class::class -> Class::class.java
                    kClass.objectInstance != null -> kClass.objectInstance
                    kClass.java.isInterface -> {
                        val proxyInterface = kClass
                        val proxyInstance = Proxy.newProxyInstance(
                                ClassLoader.getSystemClassLoader(),
                                arrayOf(proxyInterface.java)
                        ) { _, _, _ -> null }
                        register { if (it == proxyInterface) proxyInstance else null }
                        proxyInstance
                    }
                    else -> {
                        val accessibleConstructorWithoutArguments = kClass.constructors.asSequence().filter {
                            it.parameters.isEmpty() && it.isAccessible
                        }.map {
                            it as Function0<*>
                        }.firstOrNull()
                        accessibleConstructorWithoutArguments?.invoke()
                    }
                }
            }
        }

        override fun <T : Any> valueFor(kClass: KClass<T>): T =
                internalLookUp(kClass) ?: throw IllegalStateException("Please register default value for ${kClass.qualifiedName}")
    }
}

inline fun <reified T> Defaults.register(value: T) = register {
    if (T::class == it) value else null
}