package net.paslavsky.kotlin.mockito

import org.mockito.Mockito
import kotlin.reflect.KClass

/**
 * ***TODO: Describe this file***
 *
 * @author [Andrey Paslavsky](mailto:a.paslavsky@gmail.com)
 * @since 0.0.1
 */

public fun <T : Any> mock(kClass: KClass<T>, setup: Mock<T>.() -> Unit = {}): T {
    val mock = Mockito.mock(kClass.java)
    Mock<T>(mock).setup()
    return mock
}

public fun <T : Any> spy(obj: T, setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(obj)
    Mock<T>(spy).setup()
    return spy
}

public fun <T : Any> spy(classToSpy: KClass<T>, setup: Mock<T>.() -> Unit = {}): T {
    val spy = Mockito.spy(classToSpy.java)
    Mock<T>(spy).setup()
    return spy
}

public fun <T : Any> verify(mock: T, verify: Verification<T>.() -> Unit) {
    Verification(mock).verify()
}

public fun verifyNoMoreInteractions(vararg mocks: Any) = Mockito.verifyNoMoreInteractions(*mocks)
public fun verifyZeroInteractions(vararg mocks: Any) = Mockito.verifyZeroInteractions(*mocks)