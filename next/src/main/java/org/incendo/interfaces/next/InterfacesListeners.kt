package org.incendo.interfaces.next

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryCloseEvent.Reason
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin
import org.incendo.interfaces.next.Constants.SCOPE
import org.incendo.interfaces.next.click.ClickContext
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.click.CompletableClickHandler
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.view.AbstractInterfaceView
import org.incendo.interfaces.next.view.InterfaceView
import org.incendo.interfaces.next.view.PlayerInterfaceView
import java.util.*
import java.util.concurrent.TimeUnit

public class InterfacesListeners : Listener {

    public companion object {
        public fun install(plugin: Plugin) {
            Bukkit.getPluginManager().registerEvents(InterfacesListeners(), plugin)
        }

        private val VALID_REASON = EnumSet.of(
            Reason.PLAYER,
            Reason.UNKNOWN,
            Reason.PLUGIN
        )

        private val VALID_INTERACT = EnumSet.of(
            Action.LEFT_CLICK_AIR,
            Action.LEFT_CLICK_BLOCK,
            Action.RIGHT_CLICK_AIR,
            Action.RIGHT_CLICK_BLOCK
        )

        private val PLAYER_INVENTORY_RANGE = 0..40
        private const val OUTSIDE_CHEST_INDEX = -999
    }

    private val spamPrevention: Cache<UUID, Long> = CacheBuilder.newBuilder().expireAfterWrite(
        50L * 3,
        TimeUnit.MILLISECONDS
    ).build()

    @EventHandler
    public fun onClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        val reason = event.reason

        if (holder !is InterfaceView) {
            return
        }

        if (reason !in VALID_REASON) {
            return
        }

        SCOPE.launch {
            val view = convertHolderToInterfaceView(holder)
            if (view != null) {
                view.backing.closeHandlers[reason]?.invoke(reason, view)
            }
            PlayerInterfaceView.OPEN_VIEWS[event.player]?.open()
        }
    }

    @EventHandler
    public fun onClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        val view = convertHolderToInterfaceView(holder) ?: return

        val clickedPoint = clickedPoint(event) ?: return

        handleClick(view, clickedPoint, event.click, event)
    }

    private fun clickedPoint(event: InventoryClickEvent): GridPoint? {
        // not really sure why this special handling is required,
        // the ordered pane system should solve this but this is the only
        // place where it's become an issue.
        if (event.inventory.holder is Player) {
            val index = event.slot

            if (index !in PLAYER_INVENTORY_RANGE) {
                return null
            }

            val x = index / 9
            val adjustedX = PlayerPane.PANE_ORDERING.indexOf(x)
            return GridPoint(adjustedX, index % 9)
        }

        val index = event.rawSlot

        if (index == OUTSIDE_CHEST_INDEX) {
            return null
        }

        return GridPoint.at(index / 9, index % 9)
    }

    @EventHandler
    public fun onInteract(event: PlayerInteractEvent) {
        if (event.action !in VALID_INTERACT) {
            return
        }

        val player = event.player
        val view = PlayerInterfaceView.OPEN_VIEWS[player] as? AbstractInterfaceView<*, *> ?: return

        val slot = player.inventory.heldItemSlot
        val clickedPoint = GridPoint.at(3, slot)

        val click = convertAction(event.action, player.isSneaking)

        handleClick(view, clickedPoint, click, event)
    }

    private fun convertHolderToInterfaceView(holder: InventoryHolder?): AbstractInterfaceView<*, *>? {
        if (holder == null) {
            return null
        }

        if (holder is AbstractInterfaceView<*, *>) {
            return holder
        }

        if (holder !is Player) {
            return null
        }

        return PlayerInterfaceView.OPEN_VIEWS[holder] as? AbstractInterfaceView<*, *>
    }

    private fun handleClick(
        view: AbstractInterfaceView<*, *>,
        clickedPoint: GridPoint,
        click: ClickType,
        event: Cancellable
    ) {
        if (view.isProcessingClick || shouldThrottle(view.player)) {
            event.isCancelled = true
            return
        }

        view.isProcessingClick = true

        val clickContext = ClickContext(view.player, view, click)

        view.backing.clickPreprocessors
            .forEach { handler -> ClickHandler.process(handler, clickContext) }

        val clickHandler = view.pane.getRaw(clickedPoint)
            ?.clickHandler ?: ClickHandler.ALLOW

        val completedClickHandler = clickHandler
            .run { CompletableClickHandler().apply { handle(clickContext) } }
            .onComplete { view.isProcessingClick = false }

        if (!completedClickHandler.completingLater) {
            completedClickHandler.complete()
        }

        event.isCancelled = completedClickHandler.cancelled
    }

    private fun convertAction(action: Action, sneaking: Boolean): ClickType {
        if (action.isRightClick) {
            if (sneaking) {
                return ClickType.SHIFT_RIGHT
            }

            return ClickType.RIGHT
        }

        if (action.isLeftClick) {
            if (sneaking) {
                return ClickType.SHIFT_LEFT
            }

            return ClickType.LEFT
        }

        return ClickType.UNKNOWN
    }

    private fun shouldThrottle(player: Player): Boolean {
        if (spamPrevention.getIfPresent(player.uniqueId) != null) {
            return true
        } else {
            spamPrevention.put(player.uniqueId, System.currentTimeMillis())
        }

        return false
    }
}
