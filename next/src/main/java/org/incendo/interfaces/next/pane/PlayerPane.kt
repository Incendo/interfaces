package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.grid.GridPoint

public class PlayerPane : OrderedPane(PANE_ORDERING) {
    internal companion object {
        internal val PANE_ORDERING = listOf(1, 2, 3, 0, 4)

        private val OFF_HAND_SLOT = GridPoint.at(4, 4)
    }

    public val hotbar: Hotbar = Hotbar()

    public val armor: Armor = Armor()

    public var offHand: Element
        get() = get(OFF_HAND_SLOT) ?: Element.EMPTY
        set(value) = set(OFF_HAND_SLOT, value)

    // todo(josh): introduce an actual concept of subpanes?
    public inner class Hotbar {
        public operator fun set(slot: Int, value: Element): Unit = set(3, slot, value)
    }

    public inner class Armor {
        public var helmet: Element
            get() = get(4, 3) ?: Element.EMPTY
            set(value) = set(4, 3, value)

        public var chest: Element
            get() = get(4, 2) ?: Element.EMPTY
            set(value) = set(4, 2, value)

        public var leggings: Element
            get() = get(4, 1) ?: Element.EMPTY
            set(value) = set(4, 1, value)

        public var boots: Element
            get() = get(4, 0) ?: Element.EMPTY
            set(value) = set(4, 0, value)
    }
}
