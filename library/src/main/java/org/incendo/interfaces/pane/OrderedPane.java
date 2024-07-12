package org.incendo.interfaces.pane;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.element.Element;

import java.util.List;

public abstract class OrderedPane extends Pane {

    private final List<Integer> ordering;

    public OrderedPane(final List<Integer> ordering) {
        this.ordering = ordering;
    }

    public final List<Integer> ordering() {
        return this.ordering;
    }

    @Override
    public final @Nullable Element get(final int row, final int column) {
        return super.get(this.orderedRow(row), column);
    }

    @Override
    public final void put(final int row, final int column, final Element value) {
        super.put(this.orderedRow(row), column, value);
    }

    @Override
    public final boolean containsKey(final int row, final int column) {
        return super.containsKey(this.orderedRow(row), column);
    }

    private int orderedRow(final int row) {
        return this.ordering.get(row);
    }
}
