package org.incendo.interfaces.next.pane

import org.bukkit.entity.Player
import org.incendo.interfaces.next.element.CompletedElement
import org.incendo.interfaces.next.element.complete
import org.incendo.interfaces.next.grid.GridMap
import org.incendo.interfaces.next.grid.HashGridMap

internal class CompletedPane : GridMap<CompletedElement> by HashGridMap()

internal suspend fun Pane.complete(player: Player): CompletedPane {
    val pane = CompletedPane()

    forEachSuspending { row, column, element ->
        pane[row, column] = element.complete(player)
    }

    return pane
}
