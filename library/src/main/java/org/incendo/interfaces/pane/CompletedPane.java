package org.incendo.interfaces.pane;

import org.incendo.interfaces.element.CompletedElement;
import org.incendo.interfaces.grid.HashGridMap;

public class CompletedPane extends HashGridMap<CompletedElement> {

    static CompletedPane complete(Pane pane) {

    }

    static CompletedPane emptyCompletedPaneAndFill(Pane base, Integer rows) {


        val pane = convertToEmptyCompletedPane()
        val airElement =
            org.incendo.interfaces.element.CompletedElement(null, org.incendo.interfaces.click.ClickHandler.EMPTY)

        forEachInGrid(rows, COLUMNS_IN_CHEST) { row, column ->
            pane[row, column] = airElement
        }

        return pane
    }

    static CompletedPane emptyCompletedPane(Pane base) {
        if (base instanceof OrderedPane orderedBase) {
            return CompletedOrderedPane(orderedBase.getOrdering$interfaces_interfaces_library_main());
        }

        return new CompletedPane();
    }

}


//    internal open fun getRaw(vector: GridPoint): org.incendo.interfaces.element.CompletedElement? = get(vector)

internal class CompletedOrderedPane(
    private val ordering: List<Int>
) : CompletedPane() {
    override fun getRaw(vector: GridPoint): org.incendo.interfaces.element.CompletedElement? {
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


