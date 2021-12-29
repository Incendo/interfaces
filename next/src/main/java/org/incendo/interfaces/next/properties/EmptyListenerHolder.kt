package org.incendo.interfaces.next.properties

public class EmptyListenerHolder : ListenableHolder {

    override val listeners: MutableList<() -> Unit> = ArrayList()
}
