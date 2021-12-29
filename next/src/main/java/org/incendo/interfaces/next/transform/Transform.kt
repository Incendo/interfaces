package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.ListenableHolder

public fun interface Transform : (Pane) -> Unit {

    public fun listenableHolders(): Array<ListenableHolder> = arrayOf()
}
