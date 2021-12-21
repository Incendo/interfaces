package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.Transform

public class ChestInterfaceBuilder internal constructor() : InterfaceBuilder<ChestPane, ChestInterface>() {

    public var rows: Int = 0

    private val transforms: MutableCollection<Transform> = mutableListOf()

    public fun withTransform(transform: Transform) {
        transforms.add(transform)
    }

    public override fun build(): ChestInterface = ChestInterface(
        rows,
        transforms
    )
}
