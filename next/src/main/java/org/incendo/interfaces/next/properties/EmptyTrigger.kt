package org.incendo.interfaces.next.properties

public object EmptyTrigger : Trigger {

    override fun trigger() {
        // no behaviour
    }

    override fun addListener(listener: () -> Unit) {
        // no behaviour
    }
}
