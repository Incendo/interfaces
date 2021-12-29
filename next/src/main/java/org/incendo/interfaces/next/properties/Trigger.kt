package org.incendo.interfaces.next.properties

public sealed interface Trigger {

    public val listeners: MutableList<() -> Unit>

    public fun addListener(listener: () -> Unit) {
        listeners += listener
    }

    public fun trigger() {
        for (listener in listeners) {
            listener.invoke()
        }
    }
}
