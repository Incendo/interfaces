package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.ChestInterfaceView

public class ChestInterface internal constructor(
    public val rows: Int,
    override val transforms: Collection<AppliedTransform>,
) : Interface<ChestPane> {

    override fun createPane(): ChestPane = ChestPane()

    override fun open(): ChestInterfaceView {
        return ChestInterfaceView(this)
    }
}
