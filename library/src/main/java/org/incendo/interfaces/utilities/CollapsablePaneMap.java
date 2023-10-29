package org.incendo.interfaces.utilities;


import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import org.incendo.interfaces.pane.CompletedPane;
import org.incendo.interfaces.pane.Pane;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.SortedMap;

public class CollapsablePaneMap extends Int2ObjectLinkedOpenHashMap<CompletedPane> {

    private CompletedPane cachedPane = null;

    private CollapsablePaneMap(int rows, Pane basePane) {

    }

    @Override
    public CompletedPane put(final int k, final CompletedPane completedPane) {
        this.cachedPane = null;
        return super.put(k, completedPane);
    }

    public CompletedPane collapse() {
        if (this.cachedPane != null) {
            return this.cachedPane;
        }

        val pane = basePane.convertToEmptyCompletedPaneAndFill(rows)

        val current = internal.toMap().values

        current.forEach { layer ->
                layer.forEach { row, column, value ->
                pane[row, column] = value
        }
        }

        return pane
    }

}
}
