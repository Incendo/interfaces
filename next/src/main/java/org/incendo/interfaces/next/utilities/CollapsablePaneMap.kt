package org.incendo.interfaces.next.utilities

import org.incendo.interfaces.next.pane.CompletedPane

internal class CollapsablePaneMap private constructor(
    // privately pass in a map here so that we can use
    // super methods when overriding methods in the delegate.
    private val internal: MutableMap<Int, CompletedPane>
) : MutableMap<Int, CompletedPane> by internal {

    internal companion object {
        internal fun create() = CollapsablePaneMap(sortedMapOf(Comparator.reverseOrder()))
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

        val pane = CompletedPane()

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
