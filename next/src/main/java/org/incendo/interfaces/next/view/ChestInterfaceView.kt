package org.incendo.interfaces.next.view

import org.incendo.interfaces.next.interfaces.ChestInterface
import org.incendo.interfaces.next.pane.ChestPane

public class ChestInterfaceView(
    override val backing: ChestInterface
) : InterfaceView<ChestPane>()
