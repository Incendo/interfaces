package org.incendo.interfaces.next.pane

import org.bukkit.entity.Player
import org.incendo.interfaces.next.element.CompletedElement
import org.incendo.interfaces.next.element.complete
import org.incendo.interfaces.next.grid.GridMap
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.grid.HashGridMap

internal open class CompletedPane : GridMap<CompletedElement> by HashGridMap() {
    internal open fun getRaw(vector: GridPoint): CompletedElement? = get(vector)
}

internal class CompletedOrderedPane(
    private val ordering: List<Int>
) : CompletedPane() {
    override fun getRaw(vector: GridPoint): CompletedElement? {
        return get(ordering[vector.x], vector.y)
    }
}

internal suspend fun Pane.complete(player: Player): CompletedPane {
    val pane = convertToEmptyCompletedPane()

    forEachSuspending { row, column, element ->
        pane[row, column] = element.complete(player)
    }

    return pane
}

internal fun Pane.convertToEmptyCompletedPane(): CompletedPane {
    if (this is OrderedPane) {
        return CompletedOrderedPane(ordering)
    }

    return CompletedPane()
}
