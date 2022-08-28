package org.incendo.interfaces.next.click

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.incendo.interfaces.next.view.InterfaceView

public data class ClickContext(
    public val player: Player,
    public val view: InterfaceView<*>,
    public val type: ClickType
)
