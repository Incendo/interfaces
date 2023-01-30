package org.incendo.interfaces.next.utilities

import org.incendo.interfaces.next.pane.CompletedPane
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.pane.convertToEmptyCompletedPaneAndFill

internal class CollapsablePaneMap private constructor(
    private val rows: Int,
    // pass in a pane from the view so that we can persist
    // ordering, used in the listeners.
    private val basePane: Pane,
    // privately pass in a map here so that we can use
    // super methods when overriding methods in the delegate.
    private val internal: MutableMap<Int, CompletedPane>
) : MutableMap<Int, CompletedPane> by internal {

    internal companion object {
        internal fun create(rows: Int, basePane: Pane) = CollapsablePaneMap(
            rows,
            basePane,
            sortedMapOf(Comparator.reverseOrder())
        )
    }

    private var cachedPane: CompletedPane? = null

    override fun put(key: Int, value: CompletedPane): CompletedPane? {
        cachedPane = null
        return internal.put(key, value)
    }

    internal fun collapse(): CompletedPane {
        cachedPane?.let { pane ->
            return pane
        }

        val pane = basePane.convertToEmptyCompletedPaneAndFill(rows)

        values.forEach { layer ->
            layer.forEach { row, column, value ->
                pane[row, column] = value
            }
        }

        return pane
    }
}
