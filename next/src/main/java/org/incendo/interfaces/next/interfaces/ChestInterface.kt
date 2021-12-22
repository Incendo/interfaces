package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform

public class ChestInterface internal constructor(
    public val rows: Int,
    transforms: Collection<AppliedTransform>,
) : Interface<ChestPane>(transforms) {

    override fun createPane(): ChestPane = ChestPane()
}
