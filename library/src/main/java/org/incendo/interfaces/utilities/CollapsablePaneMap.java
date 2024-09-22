package org.incendo.interfaces.utilities;

import org.incendo.interfaces.pane.CompletedPane;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public final class CollapsablePaneMap extends ConcurrentSkipListMap<Integer, CompletedPane> {

    private final int rows;
    private CompletedPane cachedPane = null;

    private CollapsablePaneMap(final int rows, final Map<Integer, CompletedPane> internal) {
        super(internal);
        this.rows = rows;
    }

    public static CollapsablePaneMap create(final int rows) {
        return new CollapsablePaneMap(rows, new TreeMap<>(Collections.reverseOrder()));
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

        CompletedPane pane = CompletedPane.filled(this.rows);

        for (CompletedPane layer : this.values()) {
            pane.putAll(layer);
        }

        this.cachedPane = pane;
        return pane;
    }
}
