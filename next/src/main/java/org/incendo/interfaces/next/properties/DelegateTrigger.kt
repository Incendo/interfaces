package org.incendo.interfaces.next.properties

public class DelegateTrigger : Trigger {

    private val listeners = mutableListOf<() -> Unit>()

    override fun trigger() {
        listeners.forEach { it() }
    }

    override fun addListener(listener: () -> Unit) {
        listeners += listener
    }
}
