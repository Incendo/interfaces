package org.incendo.interfaces.next.click

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.incendo.interfaces.next.view.AbstractInterfaceView

public data class ClickContext(
    public val player: Player,
    public val view: AbstractInterfaceView<*, *>,
    public val type: ClickType
)
