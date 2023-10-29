package org.incendo.interfaces.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/** An event emitted when the inventory of [player] is drawn. */
public class DrawPaneEvent(player: Player) : PlayerEvent(player) {

    public companion object {
        @JvmStatic
        public val handlerList: HandlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}
