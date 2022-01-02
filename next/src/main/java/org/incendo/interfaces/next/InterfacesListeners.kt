package org.incendo.interfaces.next

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin
import org.incendo.interfaces.next.click.ClickContext
import org.incendo.interfaces.next.utilities.GridPoint
import org.incendo.interfaces.next.view.InterfaceView

public class InterfacesListeners : Listener {

    public companion object {
        public fun install(plugin: Plugin) {
            Bukkit.getPluginManager().registerEvents(InterfacesListeners(), plugin)
        }
    }

    @EventHandler
    public fun onClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder

        if (holder !is InterfaceView<*>) {
            return
        }

        val player = event.whoClicked as Player
        val bukkitIndex = event.slot
        val clickedPoint = GridPoint.at(bukkitIndex % 9, bukkitIndex / 9)

        holder.pane[clickedPoint]
            ?.clickHandler()
            ?.invoke(ClickContext(false, player, holder))
    }
}
