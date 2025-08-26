package org.incendo.interfaces.kotlin.paper

import org.incendo.interfaces.core.element.Element
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.util.Vector2
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.view.ChestView
import org.incendo.interfaces.paper.view.PlayerView
import org.incendo.interfaces.paper.view.TaskableView

public data class MutableChestPaneView(
    private var internalPane: ChestPane,
    private val view: ChestView,
) : Pane,
    PlayerView<ChestPane> by view,
    TaskableView by view {
    override fun elements(): MutableCollection<Element> = internalPane.elements()

    /**
     * Returns the element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element
     */
    public operator fun get(
        x: Int,
        y: Int,
    ): ItemStackElement<ChestPane> = internalPane.element(x, y)

    /**
     * Sets an element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param element the element
     */
    public operator fun set(
        x: Int,
        y: Int,
        element: ItemStackElement<ChestPane>,
    ): Unit =
        mutate {
            element(element, x, y)
        }

    /**
     * Sets an element at the given position.
     *
     * @param position the vector coordinate
     * @param element the element
     */
    public operator fun set(
        position: Vector2,
        element: ItemStackElement<ChestPane>,
    ): Unit =
        mutate {
            element(element, position.x, position.y)
        }

    private fun mutate(mutator: ChestPane.() -> ChestPane) {
        this.internalPane = internalPane.mutator()
    }

    /**
     * Converts this mutable chest pane to a [ChestPane].
     *
     * @return the converted pane
     */
    public fun toChestPane(): ChestPane = internalPane
}
