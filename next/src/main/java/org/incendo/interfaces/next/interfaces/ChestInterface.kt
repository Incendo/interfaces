package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.Transform

public class ChestInterface internal constructor(
    public val rows: Int,
    override val transforms: Collection<Transform>,
) : Interface<ChestPane> {

}
