package org.incendo.interfaces.kotlin.paper

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ClickHandler
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.type.ChestInterface

/**
 * Builds a new [ChestInterface] using the given [builder].
 *
 * @return built interface
 */
public fun buildChestInterface(builder: MutableChestInterfaceBuilder.() -> Unit): ChestInterface =
    MutableChestInterfaceBuilder().also(builder).toBuilder().build()

// <editor-fold desc="Player Extensions">
/**
 * Opens the [interface] for `this` player.
 *
 * @return the interface view
 */
public fun <T : Pane> Player.open(
    `interface`: Interface<T, PlayerViewer>
): InterfaceView<T, PlayerViewer, Interface<T, PlayerViewer>> =
    `interface`.open(PlayerViewer.of(this))

/**
 * Returns a [PlayerViewer] instance wrapping `this` player.
 *
 * @return viewer instance
 */
public fun Player.asViewer(): PlayerViewer = PlayerViewer.of(this)
// </editor-fold>

/**
 * Returns an [ItemStackElement] instance wrapping `this` item stack.
 *
 * @param handler optional click handler
 * @return element instance
 */
public fun ItemStack.asElement(handler: ClickHandler<*>? = null): ItemStackElement =
    if (handler == null) ItemStackElement.of(this) else ItemStackElement.of(this, handler)
