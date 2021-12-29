package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.properties.ListenableHolder
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public class ChestInterfaceBuilder internal constructor() : InterfaceBuilder<ChestPane, ChestInterface>() {

    public var initialTitle: Component? = null
    public var rows: Int = 0

    private val transformCounter by IncrementingInteger()
    private val transforms: MutableCollection<AppliedTransform> = mutableListOf()

    public fun withTransform(vararg listenerHolders: ListenableHolder, transform: Transform) {
        transforms.add(AppliedTransform(transformCounter, transform))
    }

    public override fun build(): ChestInterface = ChestInterface(
        rows,
        transforms
    )
}
