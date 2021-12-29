package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.ChestInterfaceView

public class ChestInterface internal constructor(
    public val rows: Int,
    override val transforms: Collection<AppliedTransform>,
    override val initialTitle: Component
) : Interface<ChestPane> {

    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override fun createPane(): ChestPane = ChestPane()

    override fun open(): ChestInterfaceView {
        return ChestInterfaceView(this)
    }
}
