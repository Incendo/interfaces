package org.incendo.interfaces

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
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
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin
import org.incendo.interfaces.Constants.SCOPE
import org.incendo.interfaces.next.click.ClickContext
import org.incendo.interfaces.click.ClickHandler
import org.incendo.interfaces.click.CompletableClickHandler
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.view.AbstractInterfaceView
import org.incendo.interfaces.next.view.InterfaceView
import org.incendo.interfaces.next.view.PlayerInterfaceView
import java.util.*
import java.util.concurrent.TimeUnit

public class InterfacesListeners private constructor() : Listener {

    public companion object {
        /** The current instance for interface listeners class. */
        public lateinit var INSTANCE: org.incendo.interfaces.InterfacesListeners
            private set

        /** Installs interfaces into this plugin. */
        public fun install(plugin: Plugin) {
            require(!org.incendo.interfaces.InterfacesListeners.Companion::INSTANCE.isInitialized) { "Already installed!" }
            org.incendo.interfaces.InterfacesListeners.Companion.INSTANCE = org.incendo.interfaces.InterfacesListeners()
            Bukkit.getPluginManager().registerEvents(org.incendo.interfaces.InterfacesListeners.Companion.INSTANCE, plugin)
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

    private val spamPrevention: Cache<UUID, Unit> = Caffeine.newBuilder()
        .expireAfterWrite(200.toLong(), TimeUnit.MILLISECONDS)
        .build()

    /** A cache of open player interface views, with weak values. */
    private val openPlayerInterfaceViews: Cache<UUID, PlayerInterfaceView> = Caffeine.newBuilder()
        .weakValues()
        .build()

    /** Returns the currently open interface for [playerId]. */
    public fun getOpenInterface(playerId: UUID): PlayerInterfaceView? =
        openPlayerInterfaceViews.getIfPresent(playerId)

    /** Updates the currently open interface for [playerId] to [view]. */
    public fun setOpenInterface(playerId: UUID, view: PlayerInterfaceView?) {
        if (view == null) {
            openPlayerInterfaceViews.invalidate(playerId)
        } else {
            openPlayerInterfaceViews.put(playerId, view)
        }
    }

    @EventHandler
    public fun onClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        val reason = event.reason

        if (holder !is InterfaceView) {
            return
        }

        SCOPE.launch {
            val view = convertHolderToInterfaceView(holder)
            if (view != null) {
                view.backing.closeHandlers[reason]?.invoke(reason, view)
            }

            if (reason !in org.incendo.interfaces.InterfacesListeners.Companion.VALID_REASON) {
                return@launch
            }

            getOpenInterface(event.player.uniqueId)?.open()
        }
    }

    @EventHandler
    public fun onClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        val view = convertHolderToInterfaceView(holder) ?: return

        val clickedPoint = clickedPoint(event) ?: return

        handleClick(view, clickedPoint, event.click, event)
    }

    @EventHandler
    public fun onPlayerQuit(event: PlayerQuitEvent) {
        setOpenInterface(event.player.uniqueId, null)
    }

    private fun clickedPoint(event: InventoryClickEvent): GridPoint? {
        // not really sure why this special handling is required,
        // the ordered pane system should solve this but this is the only
        // place where it's become an issue.
        if (event.inventory.holder is Player) {
            val index = event.slot

            if (index !in org.incendo.interfaces.InterfacesListeners.Companion.PLAYER_INVENTORY_RANGE) {
                return null
            }

            val x = index / 9
            val adjustedX = PlayerPane.PANE_ORDERING.indexOf(x)
            return GridPoint(adjustedX, index % 9)
        }

        val index = event.rawSlot

        if (index == org.incendo.interfaces.InterfacesListeners.Companion.OUTSIDE_CHEST_INDEX) {
            return null
        }

        return GridPoint.at(index / 9, index % 9)
    }

    @EventHandler
    public fun onInteract(event: PlayerInteractEvent) {
        if (event.action !in org.incendo.interfaces.InterfacesListeners.Companion.VALID_INTERACT) {
            return
        }
        if (event.hand != EquipmentSlot.HAND) {
            return
        }

        val player = event.player
        val view = getOpenInterface(player.uniqueId) as? AbstractInterfaceView<*, *> ?: return

        val slot = player.inventory.heldItemSlot
        val clickedPoint = GridPoint.at(3, slot)

        val click = convertAction(event.action, player.isSneaking)

        handleClick(view, clickedPoint, click, event)
    }

    /**
     * Converts an inventory holder to an [AbstractInterfaceView] if possible. If the holder is a player
     * their currently open player interface is returned.
     */
    public fun convertHolderToInterfaceView(holder: InventoryHolder?): AbstractInterfaceView<*, *>? {
        if (holder == null) {
            return null
        }

        // If it's an abstract view use that one
        if (holder is AbstractInterfaceView<*, *>) {
            return holder
        }

        // If it's the player's own inventory use the held one
        if (holder is HumanEntity) {
            return getOpenInterface(holder.uniqueId)
        }

        return null
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
            .forEach { handler -> org.incendo.interfaces.click.ClickHandler.process(handler, clickContext) }

        val clickHandler = view.pane.getRaw(clickedPoint)
            ?.clickHandler ?: org.incendo.interfaces.click.ClickHandler.ALLOW

        val completedClickHandler = clickHandler
            .run { org.incendo.interfaces.click.CompletableClickHandler().apply { handle(clickContext) } }
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

    private fun shouldThrottle(player: Player): Boolean =
        if (spamPrevention.getIfPresent(player.uniqueId) == null) {
            spamPrevention.put(player.uniqueId, Unit)
            false
        } else {
            true
        }
}
