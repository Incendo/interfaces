package org.incendo.interfaces.kotlin.paper

import org.incendo.interfaces.core.element.Element
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.util.Vector2
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.CombinedPane
import org.incendo.interfaces.paper.view.CombinedView
import org.incendo.interfaces.paper.view.PlayerView
import org.incendo.interfaces.paper.view.TaskableView

public data class MutableCombinedPaneView(
    private var internalPane: CombinedPane,
    private val view: CombinedView,
) : Pane,
    PlayerView<CombinedPane> by view,
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
    ): ItemStackElement<CombinedPane> = internalPane.element(x, y)

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
        element: ItemStackElement<CombinedPane>,
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
        element: ItemStackElement<CombinedPane>,
    ): Unit =
        mutate {
            element(element, position.x, position.y)
        }

    public fun hotbar(x: Int): ItemStackElement<CombinedPane> = internalPane.hotbar(x)

    public fun hotbar(
        x: Int,
        element: ItemStackElement<CombinedPane>,
    ): Unit =
        mutate {
            hotbar(element, x)
        }

    private fun mutate(mutator: CombinedPane.() -> CombinedPane) {
        this.internalPane = internalPane.mutator()
    }

    /**
     * Converts this mutable chest pane to a [CombinedPane].
     *
     * @return the converted pane
     */
    public fun toCombinedPane(): CombinedPane = internalPane
}
