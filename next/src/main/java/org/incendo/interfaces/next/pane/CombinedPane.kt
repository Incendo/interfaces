package org.incendo.interfaces.next.pane

public class CombinedPane(
    rows: Int
) : OrderedPane(createMappings(rows)) {

    private companion object {
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
}
