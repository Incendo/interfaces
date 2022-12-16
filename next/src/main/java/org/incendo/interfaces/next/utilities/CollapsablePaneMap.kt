package org.incendo.interfaces.next.utilities

import org.incendo.interfaces.next.pane.Pane

internal class CollapsablePaneMap private constructor(
    // privately pass in a map here so that we can use
    // super methods when overriding methods in the delegate.
    private val internal: MutableMap<Int, Pane>
) : MutableMap<Int, Pane> by internal {

    internal companion object {
        internal fun create() = CollapsablePaneMap(sortedMapOf(Comparator.reverseOrder()))
    }

    private var cachedPane: Pane? = null

    override fun put(key: Int, value: Pane): Pane? {
        cachedPane = null
        return internal.put(key, value)
    }

    internal fun collapse(): Pane {
        cachedPane?.let { pane ->
            return pane
        }

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
