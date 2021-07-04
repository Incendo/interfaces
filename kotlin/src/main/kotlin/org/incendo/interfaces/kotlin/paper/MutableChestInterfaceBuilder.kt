package org.incendo.interfaces.kotlin.paper

import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.incendo.interfaces.core.transform.Transform
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.element.ClickHandler
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.type.ChestInterface
import org.incendo.interfaces.paper.view.InventoryView

public class MutableChestInterfaceBuilder {

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
    public var clickHandler: ClickHandler<ChestPane>
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
        handler: (InventoryClickEvent, InventoryView<ChestPane>) -> Unit
    ): Unit = mutate { internalBuilder.clickHandler(handler) }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    public fun addTransform(transform: Transform<ChestPane>): Unit = mutate {
        internalBuilder.addTransform(transform)
    }

    /**
     * Adds the given [transform] to the interface.
     *
     * @param transform transform to add
     */
    public fun addTransform(
        transform: (ChestPane, InterfaceView<ChestPane, *, *>) -> ChestPane
    ): Unit = mutate { internalBuilder.addTransform(transform) }
    // </editor-fold>

    private fun mutate(mutator: ChestInterface.Builder.() -> ChestInterface.Builder) {
        this.internalBuilder = internalBuilder.mutator()
    }

    /**
     * Converts this mutable chest interface builder to a [ChestInterface.Builder].
     *
     * @return The converted builder.
     */
    public fun toBuilder(): ChestInterface.Builder = internalBuilder
}
