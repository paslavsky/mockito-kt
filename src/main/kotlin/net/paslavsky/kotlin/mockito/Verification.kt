package net.paslavsky.kotlin.mockito

import org.mockito.Mockito

/**
 * ***TODO: Describe this file***
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