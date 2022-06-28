package org.incendo.interfaces.next.properties

public sealed interface Trigger {
    public fun trigger()

    public fun addListener(listener: () -> Unit)
}
