package org.incendo.interfaces.next

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin
import org.incendo.interfaces.next.click.ClickContext
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.view.InterfaceView

public class InterfacesListeners : Listener {

    public companion object {
        public fun install(plugin: Plugin) {
            Bukkit.getPluginManager().registerEvents(InterfacesListeners(), plugin)
        }
    }

    @EventHandler
    public fun onClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder

        if (holder !is InterfaceView<*>) {
            return
        }

        holder.close()
    }

    @EventHandler
    public fun onClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder

        if (holder !is InterfaceView<*>) {
            return
        }

        if (holder.isProcessingClick) {
            event.isCancelled = true
            return
        }

        holder.isProcessingClick = true

        val player = event.whoClicked as Player
        val bukkitIndex = event.slot
        val clickedPoint = GridPoint.at(bukkitIndex / 9, bukkitIndex % 9)

        val clickContext = ClickContext(true, player, holder, event.click)

        holder.backing.clickPreprocessors
            .forEach { handler -> handler.handleSynchronous(clickContext) }

        println(holder.pane[clickedPoint]?.drawable()?.draw(player)?.type)

        holder.pane[clickedPoint]
            ?.clickHandler()
            ?.handleAsynchronous(clickContext)
            ?.thenRun { holder.isProcessingClick = false }

        if (clickContext.cancelled) {
            event.isCancelled = true
        }
    }
}
