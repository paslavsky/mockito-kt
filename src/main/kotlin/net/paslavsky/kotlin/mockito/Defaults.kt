package net.paslavsky.kotlin.mockito

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.jvm.isAccessible

/**
 * ***TODO: Describe this file***
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */
public open class Defaults {
    private val search = LinkedList< (kotlin.Function1<KClass<*>, Any?>)>()

    public open fun <T : Any> valueFor(kClass: KClass<T>) : T = internalLookUp(kClass) ?: Global.valueFor(kClass)

    public fun register(lookUpValueFor: (KClass<*>) -> Any?) {
        search.add(lookUpValueFor)
    }

    public fun register(pair: Pair<KClass<*>, Any>) {
        search.add { if (pair.first == it) pair.second else null }
    }

    internal fun <T : Any> internalLookUp(kClass: KClass<T>): T? = search.asSequence().map {
        it.invoke(kClass)
    }.filterNotNull().filter {
        kClass.java.isInstance(it)
    }.map {
        it as T
    }.firstOrNull()

    public companion object Global : Defaults() {
        init {
            register {
                when (it) {
                    Unit::class -> Unit
                    Any::class -> Any()
                    Boolean::class -> 0
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
            register {
                when (it) {
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
            register {
                when (it) {
                    Locale::class -> Locale.getDefault()
                    else -> null
                }
            }
            register {
                when {
                    it == KClass::class -> KClass::class
                    it == Class::class -> Class::class.java
                    it.objectInstance != null -> it.objectInstance
                    else -> {
                        val accessibleConstructorWithoutArguments = it.constructors.asSequence().filter {
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