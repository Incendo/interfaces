package org.incendo.interfaces.utilities;

import org.incendo.interfaces.pane.CompletedPane;
import org.incendo.interfaces.pane.Pane;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class CollapsablePaneMap extends LinkedHashMap<Integer, CompletedPane> {

    private final int rows;
    private final Pane basePane;
    private CompletedPane cachedPane = null;

    private CollapsablePaneMap(final int rows, final Pane basePane, final Map<Integer, CompletedPane> internal) {
        super(internal);
        this.rows = rows;
        this.basePane = basePane;
    }

    public static CollapsablePaneMap create(final int rows, final Pane basePane) {
        return new CollapsablePaneMap(rows, basePane, new TreeMap<>(Collections.reverseOrder()));
    }

    @Override
    public CompletedPane put(final Integer key, final CompletedPane value) {
        this.cachedPane = null;
        return super.put(key, value);
    }

    public CompletedPane collapse() {
        if (this.cachedPane != null) {
            return this.cachedPane;
        }

        CompletedPane pane = CompletedPane.filled(this.basePane, this.rows);

        for (CompletedPane layer : this.values()) {
            pane.putAll(layer);
        }

        this.cachedPane = pane;
        return pane;
    }
}
