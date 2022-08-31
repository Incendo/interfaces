package org.incendo.interfaces.kotlin.paper

import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.transform.InterfaceProperty
import org.incendo.interfaces.core.transform.ReactiveTransform
import org.incendo.interfaces.core.transform.Transform
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.kotlin.MutableInterfaceBuilder
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.click.InventoryClickContext
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.type.ChestInterface
import org.incendo.interfaces.paper.type.CloseHandler
import org.incendo.interfaces.paper.view.ChestView
import org.incendo.interfaces.paper.view.PlayerView

@Suppress("unused")
public class MutableChestInterfaceBuilder :
    MutableInterfaceBuilder<
        ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane, ChestView>> {

    private var internalBuilder: ChestInterface.Builder = ChestInterface.builder()

    // <editor-fold desc="Mutable Properties">
    /** The number of rows for the interface. */
    public var rows: Int
        get() = internalBuilder.rows()
        set(value) = mutate { internalBuilder.rows(value) }

    /** The title of the interface. */
    public var title: Component
        get() = internalBuilder.title()
        set(value) = mutate { internalBuilder.title(value) }

    /** The click handler of the interface. */
    public var clickHandler:
        ClickHandler<
            ChestPane,
            InventoryClickEvent,
            PlayerViewer,
            InventoryClickContext<ChestPane, ChestView>>
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
     * Sets the click handler.
     *
     * @param handler click handler
     */
    public fun clickHandler(
        handler:
            ClickHandler<
                ChestPane,
                InventoryClickEvent,
                PlayerViewer,
                InventoryClickContext<ChestPane, ChestView>>
    ): Unit = mutate { internalBuilder.clickHandler(handler) }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     * @param priority the priority, defaults to `1`
     */
    public fun addTransform(
        transform: Transform<ChestPane, PlayerViewer>,
        priority: Int = 1
    ): Unit = mutate {
        if (transform is ReactiveTransform<ChestPane, PlayerViewer, *>) {
            internalBuilder.addReactiveTransform(priority, transform) as ChestInterface.Builder
        } else {
            internalBuilder.addTransform(priority, transform, InterfaceProperty.dummy())
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
        transform: (ChestPane, ChestView) -> ChestPane
    ): Unit = mutate {
        return@mutate internalBuilder.addTransform(
            priority,
            transform as (ChestPane, InterfaceView<ChestPane, *>) -> ChestPane,
            *properties
        )
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    public fun withTransform(
        priority: Int = 1,
        vararg properties: InterfaceProperty<*>,
        transform: (MutableChestPaneView) -> Unit
    ) {
        addTransform(priority, *properties) { chestPane, interfaceView ->
            MutableChestPaneView(chestPane, interfaceView).also(transform).toChestPane()
        }
    }

    /**
     * Adds the given [handler] to the interface.
     *
     * @param handler close handler to add
     */
    public fun addCloseHandler(handler: CloseHandler<ChestPane>): Unit = mutate {
        internalBuilder.addCloseHandler(handler)
    }

    /**
     * Adds the given [handler] to the interface.
     *
     * @param handler close handler to add
     */
    @Suppress("unchecked_cast")
    public fun withCloseHandler(handler: (InventoryCloseEvent, ChestView) -> Unit) {
        addCloseHandler(handler as (InventoryCloseEvent, PlayerView<ChestPane>) -> Unit)
    }
    // </editor-fold>

    private fun mutate(mutator: ChestInterface.Builder.() -> ChestInterface.Builder) {
        this.internalBuilder = internalBuilder.mutator()
    }

    /**
     * Converts this mutable chest interface builder to a [ChestInterface.Builder].
     *
     * @return the converted builder
     */
    public fun toBuilder(): ChestInterface.Builder = internalBuilder
}
