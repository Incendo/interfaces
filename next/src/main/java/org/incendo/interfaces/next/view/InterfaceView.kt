package org.incendo.interfaces.next.view

import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.Update
import org.incendo.interfaces.next.utilities.CollapsablePaneMap

public abstract class InterfaceView<P : Pane> {

    public abstract val backing: Interface<P>

    private val panes = CollapsablePaneMap()
    private val pane: Pane

    init {
        update(CompleteUpdate)
        pane = panes.collapse()
    }

    public fun update(update: Update) {
        update.apply(this)
    }

    internal fun applyTransform(transform: AppliedTransform) {
        val pane = backing.createPane()
        transform(pane)

        panes[transform.priority] = pane
    }
}
