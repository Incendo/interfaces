package org.incendo.interfaces.next.utilities

import org.incendo.interfaces.next.pane.Pane

public class CollapsablePaneMap : MutableMap<Int, Pane> by sortedMapOf<Int, Pane>(Comparator.reverseOrder()) {

    public fun collapse(): Pane {
        val pane = Pane()

        values.forEach { layer ->
            layer.forEach { column, row, value ->
                if (!pane.has(column, row)) {
                    pane[column, row] = value
                }
            }
        }

        return pane
    }
}
