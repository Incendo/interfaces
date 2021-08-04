package org.incendo.interfaces.kotlin.paper

import org.bukkit.event.inventory.InventoryClickEvent
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.transform.InterfaceProperty
import org.incendo.interfaces.core.transform.ReactiveTransform
import org.incendo.interfaces.core.transform.Transform
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.kotlin.MutableInterfaceBuilder
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.click.InventoryClickContext
import org.incendo.interfaces.paper.pane.PlayerPane
import org.incendo.interfaces.paper.type.PlayerInterface
import org.incendo.interfaces.paper.view.PlayerInventoryView
import org.incendo.interfaces.paper.view.PlayerView

@Suppress("unused")
public class MutablePlayerInterfaceBuilder :
    MutableInterfaceBuilder<
        PlayerPane,
        InventoryClickEvent,
        PlayerViewer,
        InventoryClickContext<PlayerPane, PlayerInventoryView>> {

    private var internalBuilder: PlayerInterface.Builder = PlayerInterface.builder()

    // <editor-fold desc="Mutable Properties">
    /** The click handler of the interface. */
    public var clickHandler:
        ClickHandler<
            PlayerPane,
            InventoryClickEvent,
            PlayerViewer,
            InventoryClickContext<PlayerPane, PlayerInventoryView>>
        get() = internalBuilder.clickHandler()
        set(value) = mutate { internalBuilder.clickHandler(value) }
    // </editor-fold>

    // <editor-fold desc="Mutating Functions">
    /**
     * Sets whether the interface should update.
     *
     * @param toggle true if the interface should update, false if not
     * @param interval how many ticks to wait between updates
     */
    public fun updates(toggle: Boolean = true, interval: Int = 1): Unit = mutate {
        internalBuilder.updates(toggle, interval)
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     * @param priority the priority
     */
    public fun addTransform(
        priority: Int = 1,
        transform: Transform<PlayerPane, PlayerViewer>
    ): Unit = mutate {
        if (transform is ReactiveTransform<PlayerPane, PlayerViewer, *>) {
            internalBuilder.addReactiveTransform(priority, transform) as PlayerInterface.Builder
        } else {
            internalBuilder.addTransform(priority, transform)
        }
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    @Suppress("unchecked_cast")
    public fun addTransform(
        priority: Int = 1,
        vararg properties: InterfaceProperty<*>,
        transform: (PlayerPane, PlayerView<PlayerPane>) -> PlayerPane
    ): Unit = mutate {
        internalBuilder.addTransform(
            priority,
            transform as (PlayerPane, InterfaceView<PlayerPane, *>) -> PlayerPane,
            *properties
        )
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    public fun withTransform(
        vararg properties: InterfaceProperty<*>,
        transform: (MutablePlayerPaneView) -> Unit
    ) {
        addTransform(1, *properties) { PlayerPane, interfaceView ->
            MutablePlayerPaneView(PlayerPane, interfaceView).also(transform).toPlayerPane()
        }
    }
    // </editor-fold>

    private fun mutate(mutator: PlayerInterface.Builder.() -> PlayerInterface.Builder) {
        this.internalBuilder = internalBuilder.mutator()
    }

    /**
     * Converts this mutable player interface builder to a [PlayerInterface.Builder].
     *
     * @return the converted builder
     */
    public fun toBuilder(): PlayerInterface.Builder = internalBuilder
}
