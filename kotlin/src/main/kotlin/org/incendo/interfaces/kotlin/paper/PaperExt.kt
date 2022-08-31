package org.incendo.interfaces.kotlin.paper

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.arguments.InterfaceArguments
import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.pane.CombinedPane
import org.incendo.interfaces.paper.type.ChestInterface
import org.incendo.interfaces.paper.type.CombinedInterface
import org.incendo.interfaces.paper.type.PlayerInterface
import org.incendo.interfaces.paper.type.TitledInterface

/**
 * Builds a new [ChestInterface] using the given [builder].
 *
 * @return built interface
 */
public fun buildChestInterface(builder: MutableChestInterfaceBuilder.() -> Unit): ChestInterface =
    MutableChestInterfaceBuilder().also(builder).toBuilder().build()

/**
 * Builds a new [PlayerInterface] using the given builder.
 *
 * @return build interface
 */
public fun buildPlayerInterface(
    builder: MutablePlayerInterfaceBuilder.() -> Unit
): PlayerInterface = MutablePlayerInterfaceBuilder().also(builder).toBuilder().build()

/**
 * Builds a new [CombinedInterface] using the given builder.
 *
 * @return build interface
 */
public fun buildCombinedInterface(
    builder: MutableCombinedInterfaceBuilder.() -> Unit
): CombinedInterface = MutableCombinedInterfaceBuilder().also(builder).toBuilder().build()

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
    arguments: InterfaceArguments? = null,
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
    arguments: InterfaceArguments? = null,
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
public fun <T : Pane> ItemStack.asElement(
    handler:
        GenericClickHandler<T>? = null
): ItemStackElement<T> = if (handler == null) ItemStackElement.of(this) else ItemStackElement.of(this, handler)

public typealias GenericClickHandler<P> =
    ClickHandler<P, InventoryClickEvent, PlayerViewer, ClickContext<P, InventoryClickEvent, PlayerViewer>>

public typealias ChestClickHandler = GenericClickHandler<ChestPane>

public typealias CombinedClickHandler = GenericClickHandler<CombinedPane>
