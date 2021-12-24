package org.incendo.interfaces.next.click

import org.bukkit.entity.Player
import org.incendo.interfaces.next.view.InterfaceView

public data class ClickContext(
    public var cancelled: Boolean,
    public val player: Player,
    public val view: InterfaceView<*>
)
