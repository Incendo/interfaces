package org.incendo.interfaces.kotlin.paper

import org.incendo.interfaces.core.element.Element
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.PlayerPane
import org.incendo.interfaces.paper.utils.PaperUtils
import org.incendo.interfaces.paper.view.PlayerView

public data class MutablePlayerPaneView(
    private var internalPane: PlayerPane,
    private val view: PlayerView<PlayerPane>
) : Pane, PlayerView<PlayerPane> by view {

    override fun elements(): MutableCollection<Element> = internalPane.elements()

    /** Access to the hotbar slots. */
    public val hotbar: ElementAccess = ElementAccess(PlayerPane.SlotType.HOTBAR)

    /** Access to the main slots. */
    public val main: ElementAccess = ElementAccess(PlayerPane.SlotType.MAIN)

    /** Access to the armor slots. */
    public val armor: ElementAccess = ElementAccess(PlayerPane.SlotType.ARMOR)

    /** Access to the off hand element. */
    public var offHand: ItemStackElement<PlayerPane>
        get() = internalPane.offHand()
        set(value) = mutate { internalPane.offHand(value) }

    private fun mutate(mutator: PlayerPane.() -> PlayerPane) {
        this.internalPane = internalPane.mutator()
    }

    /**
     * Converts this mutable player pane to a [PlayerPane]
     *
     * @return the converted pane
     */
    public fun toPlayerPane(): PlayerPane = internalPane

    public inner class ElementAccess
    internal constructor(private val slotType: PlayerPane.SlotType) {

        /**
         * Returns the element in the given [slot].
         *
         * @return the element
         */
        public operator fun get(slot: Int): ItemStackElement<PlayerPane> =
            internalPane.getAdjusted(slot, slotType)

        /**
         * Returns the element at the given [x],[y]-coordinates.
         *
         * @return the element
         */
        public operator fun get(x: Int, y: Int): ItemStackElement<PlayerPane> =
            get(PaperUtils.gridToSlot(x, y))

        /**
         * Sets the [element] in the given [slot].
         *
         * @param slot the slot
         * @param element the element
         */
        public operator fun set(slot: Int, element: ItemStackElement<PlayerPane>) {
            mutate { internalPane.setAdjusted(slot, slotType, element) }
        }

        /**
         * Sets the element at the given [x],[y]-coordinates.
         *
         * @param x the x coordinate
         * @param y the y coordinate
         * @param element the element
         */
        public operator fun set(x: Int, y: Int, element: ItemStackElement<PlayerPane>) {
            set(PaperUtils.gridToSlot(x, y), element)
        }
    }
}
