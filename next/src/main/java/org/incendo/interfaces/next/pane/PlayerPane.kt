package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element

public class PlayerPane : OrderedPane(PANE_ORDERING) {
    private companion object {
        private val PANE_ORDERING = listOf(1, 2, 3, 0)
    }

    public val hotbar: Hotbar = Hotbar()

    // todo(josh): introduce an actual concept of subpanes?
    public inner class Hotbar {
        public operator fun set(slot: Int, value: Element): Unit = set(0, slot, value)
    }
}
