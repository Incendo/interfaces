package org.incendo.interfaces.next.utilities

import org.incendo.interfaces.next.pane.Pane

public class CollapsablePaneMap : MutableMap<Int, Pane> by sortedMapOf<Int, Pane>(Comparator.reverseOrder()) {

    public fun collapse(): Pane {
        val pane = Pane()

        values.forEach { layer ->
            layer.forEach { row, column, value ->
                if (!pane.has(row, column)) {
                    pane[row, column] = value
                }
            }
        }

        return pane
    }
}
