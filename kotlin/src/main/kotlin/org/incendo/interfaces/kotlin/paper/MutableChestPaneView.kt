package org.incendo.interfaces.kotlin.paper

import org.incendo.interfaces.core.element.Element
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.ChestPane

public class MutableChestPaneView(
    private var internalPane: ChestPane,
    private val view: InterfaceView<ChestPane, PlayerViewer>
) : Pane, InterfaceView<ChestPane, PlayerViewer> by view {

    override fun elements(): MutableCollection<Element> = internalPane.elements()

    /**
     * Returns the element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element
     */
    public operator fun get(x: Int, y: Int): ItemStackElement = internalPane.element(x, y)

    /**
     * Sets an element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param element the element
     */
    public operator fun set(x: Int, y: Int, element: ItemStackElement): Unit = mutate {
        element(element, x, y)
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
