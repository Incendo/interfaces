package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.utilities.forEachInGrid
import org.incendo.interfaces.next.view.AbstractInterfaceView.Companion.COLUMNS_IN_CHEST

public class CombinedPane(
    private val chestRows: Int
) : OrderedPane(createMappings(chestRows)) {

    private companion object {
        private const val PLAYER_INVENTORY_ROWS = 4

        private fun createMappings(rows: Int): List<Int> = buildList {
            IntRange(0, rows - 1).forEach(::add)

            // the players hotbar is row 0 in the players inventory,
            // for combined interfaces it makes more sense for hotbar
            // to be the last row, so reshuffle here.
            add(rows + 1)
            add(rows + 2)
            add(rows + 3)
            add(rows)
        }
    }

    override fun fill(element: Element) {
        val rows = chestRows + PLAYER_INVENTORY_ROWS

        forEachInGrid(rows, COLUMNS_IN_CHEST) { row, column ->
            this[row, column] = element
        }
    }
}
