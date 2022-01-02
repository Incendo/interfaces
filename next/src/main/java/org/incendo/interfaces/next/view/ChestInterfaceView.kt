package org.incendo.interfaces.next.view

import org.bukkit.entity.Player
import org.incendo.interfaces.next.interfaces.ChestInterface
import org.incendo.interfaces.next.pane.ChestPane

public class ChestInterfaceView(
    player: Player,
    backing: ChestInterface
) : InterfaceView<ChestPane>(
    player,
    backing
)
