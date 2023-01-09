package org.incendo.interfaces.next.properties

public interface Trigger {
    public fun trigger()

    public fun addListener(listener: () -> Unit)
}
