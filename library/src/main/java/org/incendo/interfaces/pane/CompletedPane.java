package org.incendo.interfaces.pane;

import org.bukkit.entity.Player;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.element.CompletedElement;
import org.incendo.interfaces.grid.HashGridMap;

import static org.incendo.interfaces.utilities.BukkitInventoryUtilities.forEachInGrid;
import static org.incendo.interfaces.view.AbstractInterfaceView.COLUMNS_IN_CHEST;

public class CompletedPane extends HashGridMap<CompletedElement> {

    public static CompletedPane complete(final Pane pane, final Player player) {
        CompletedPane empty = new CompletedPane();

        pane.forEach((position, element) -> {
            empty.put(position, element.complete(player));
        });

        return empty;
    }

    public static CompletedPane filled(final Integer rows) {
        CompletedPane pane = new CompletedPane();
        CompletedElement air = new CompletedElement(null, ClickHandler.EMPTY);

        forEachInGrid(rows, COLUMNS_IN_CHEST, (row, column) -> {
            pane.put(row, column, air);
        });

        return pane;
    }
}
