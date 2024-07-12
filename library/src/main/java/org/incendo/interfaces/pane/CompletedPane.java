package org.incendo.interfaces.pane;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.element.CompletedElement;
import org.incendo.interfaces.grid.HashGridMap;

import java.util.List;

import static org.incendo.interfaces.utilities.BukkitInventoryUtilities.forEachInGrid;
import static org.incendo.interfaces.view.AbstractInterfaceView.COLUMNS_IN_CHEST;

public class CompletedPane extends HashGridMap<CompletedElement> {

    public static CompletedPane complete(final Pane pane, final Player player) {
        CompletedPane empty = empty(pane);

        pane.forEach((position, element) -> {
            empty.put(position, element.complete(player));
        });

        return empty;
    }

    public static CompletedPane filled(final Pane base, final Integer rows) {
        CompletedPane pane = empty(base);
        CompletedElement air = new CompletedElement(null, ClickHandler.EMPTY);

        forEachInGrid(rows, COLUMNS_IN_CHEST, (row, column) -> {
            pane.put(row, column, air);
        });

        return pane;
    }

    public static CompletedPane empty(final Pane base) {
        if (base instanceof OrderedPane orderedBase) {
            return new CompletedOrderedPane(orderedBase.ordering());
        }

        return new CompletedPane();
    }

    //todo: investigate why this is needed
    public final @Nullable CompletedElement getRaw(final int row, final int column) {
        return super.get(row, column);
    }

    public static final class CompletedOrderedPane extends CompletedPane {
        private final List<Integer> ordering;

        public CompletedOrderedPane(final List<Integer> ordering) {
            this.ordering = ordering;
        }

        @Override
        public @Nullable CompletedElement get(final int row, final int column) {
            return super.get(this.ordering.get(row), column);
        }

    }
}
