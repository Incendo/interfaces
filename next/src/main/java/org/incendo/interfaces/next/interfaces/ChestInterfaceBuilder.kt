package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.PrioritizedTransform
import org.incendo.interfaces.next.transform.Transform

public class ChestInterfaceBuilder internal constructor() : InterfaceBuilder<ChestPane, ChestInterface>() {

    public var rows: Int = 0

    private val transforms: MutableCollection<PrioritizedTransform> = mutableListOf()

    public fun withTransform(priority: Int = 0, transform: Transform) {
        transforms.add(PrioritizedTransform(priority, transform))
    }

    public override fun build(): ChestInterface = ChestInterface(
        rows,
        transforms
    )
}
