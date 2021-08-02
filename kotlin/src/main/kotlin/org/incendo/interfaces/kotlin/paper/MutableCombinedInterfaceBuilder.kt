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
import org.incendo.interfaces.paper.pane.CombinedPane
import org.incendo.interfaces.paper.type.CloseHandler
import org.incendo.interfaces.paper.type.CombinedInterface
import org.incendo.interfaces.paper.view.CombinedView
import org.incendo.interfaces.paper.view.PlayerView

@Suppress("unused")
public class MutableCombinedInterfaceBuilder :
    MutableInterfaceBuilder<
        CombinedPane,
        InventoryClickEvent,
        PlayerViewer,
        InventoryClickContext<CombinedPane, CombinedView>> {

    private var internalBuilder: CombinedInterface.Builder = CombinedInterface.builder()

    // <editor-fold desc="Mutable Properties">
    /** The number of rows for the interface. */
    public var chestRows: Int
        get() = internalBuilder.chestRows()
        set(value) = mutate { internalBuilder.chestRows(value) }

    /** The title of the interface. */
    public var title: Component
        get() = internalBuilder.title()
        set(value) = mutate { internalBuilder.title(value) }

    /** The click handler of the interface. */
    public var clickHandler:
        ClickHandler<
            CombinedPane,
            InventoryClickEvent,
            PlayerViewer,
            InventoryClickContext<CombinedPane, CombinedView>>
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
                CombinedPane,
                InventoryClickEvent,
                PlayerViewer,
                InventoryClickContext<CombinedPane, CombinedView>>
    ): Unit = mutate { internalBuilder.clickHandler(handler) }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     * @param priority the priority, defaults to `1`
     */
    public fun addTransform(
        transform: Transform<CombinedPane, PlayerViewer>,
        priority: Int = 1
    ): Unit = mutate {
        if (transform is ReactiveTransform<CombinedPane, PlayerViewer, *>) {
            internalBuilder.addReactiveTransform(priority, transform) as CombinedInterface.Builder
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
        transform: (CombinedPane, CombinedView) -> CombinedPane
    ): Unit = mutate {
        internalBuilder.addTransform(
            priority,
            transform as (CombinedPane, InterfaceView<CombinedPane, *>) -> CombinedPane,
            *properties)
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    public fun withTransform(
        priority: Int = 1,
        vararg properties: InterfaceProperty<*>,
        transform: (MutableCombinedPaneView) -> Unit
    ) {
        addTransform(priority, *properties) { combinedPane, interfaceView ->
            MutableCombinedPaneView(combinedPane, interfaceView).also(transform).toCombinedPane()
        }
    }

    /**
     * Adds the given [handler] to the interface.
     *
     * @param handler close handler to add
     */
    public fun addCloseHandler(handler: CloseHandler<CombinedPane>): Unit = mutate {
        internalBuilder.addCloseHandler(handler)
    }

    /**
     * Adds the given [handler] to the interface.
     *
     * @param handler close handler to add
     */
    @Suppress("unchecked_cast")
    public fun withCloseHandler(handler: (InventoryCloseEvent, CombinedView) -> Unit) {
        addCloseHandler(handler as (InventoryCloseEvent, PlayerView<CombinedPane>) -> Unit)
    }
    // </editor-fold>

    private fun mutate(mutator: CombinedInterface.Builder.() -> CombinedInterface.Builder) {
        this.internalBuilder = internalBuilder.mutator()
    }

    /**
     * Converts this mutable chest interface builder to a [CombinedInterface.Builder].
     *
     * @return the converted builder
     */
    public fun toBuilder(): CombinedInterface.Builder = internalBuilder
}
