package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.PrioritizedTransform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public abstract class Interface<P : Pane> internal constructor(
    pendingTransforms: Collection<PrioritizedTransform>
) {

    private val transformCounter by IncrementingInteger()
    private val panes = HashMap<Int, MutableMap<Int, Pane>>()

    internal val transforms = pendingTransforms.map { prioritizedTransform ->
        AppliedTransform(transformCounter, prioritizedTransform.priority, prioritizedTransform.transform)
    }

    internal fun applyTransform(transform: AppliedTransform) {
        val pane = createPane()
        transform(pane)

        val layer = panes.computeIfAbsent(transform.priority) { HashMap() }
        layer[transform.id] = pane
    }

    protected abstract fun createPane(): P
}
