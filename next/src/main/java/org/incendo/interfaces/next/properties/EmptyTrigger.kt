package org.incendo.interfaces.next.properties

public class EmptyTrigger : Trigger {

    override val listeners: MutableList<() -> Unit> = ArrayList()
}
