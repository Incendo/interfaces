package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.utilities.CollapsablePaneMap

public abstract class Interface<P : Pane> internal constructor(
    internal val transforms: Collection<AppliedTransform>
) {

    private val panes = CollapsablePaneMap()

    internal fun applyTransform(transform: AppliedTransform) {
        val pane = createPane()
        transform(pane)

        panes[transform.priority] = pane
    }

    protected abstract fun createPane(): P
}
