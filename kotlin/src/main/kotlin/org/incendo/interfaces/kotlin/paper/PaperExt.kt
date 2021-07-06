package org.incendo.interfaces.kotlin.paper

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.arguments.InterfaceArgument
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ClickHandler
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.type.ChestInterface
import org.incendo.interfaces.paper.type.TitledInterface

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
 * @param `interface` interface to open
 * @param arguments the arguments to pass to the interface
 * @return the interface view
 */
public fun <T : Pane> Player.open(
    `interface`: Interface<T, PlayerViewer>,
    arguments: InterfaceArgument? = null,
): InterfaceView<T, PlayerViewer> =
    if (arguments == null) `interface`.open(this.asViewer())
    else `interface`.open(this.asViewer(), arguments)

/**
 * Opens the [interface] for `this` player.
 *
 * @param `interface` interface to open
 * @param arguments the arguments to pass to the interface
 * @param title the title
 * @return the interface view
 */
public fun <T : Pane, I : TitledInterface<T, PlayerViewer>> Player.open(
    `interface`: I,
    arguments: InterfaceArgument? = null,
    title: Component = `interface`.title()
): InterfaceView<T, PlayerViewer> =
    if (arguments == null) `interface`.open(this.asViewer(), title)
    else `interface`.open(this.asViewer(), arguments, title)

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
