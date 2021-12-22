package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.PrioritizedTransform

public class ChestInterface internal constructor(
    public val rows: Int,
    transforms: Collection<PrioritizedTransform>,
) : Interface<ChestPane>(transforms) {

    override fun createPane(): ChestPane = ChestPane()
}
