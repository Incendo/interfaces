package org.incendo.interfaces.grid;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public interface GridMap<V> extends Map<GridPoint, V> {

    default void put(int row, int column, V value) {
        this.put(GridPoint.at(row, column), value);
    }

    default V get(int row, int column) {
        return this.get(GridPoint.at(row, column));
    }

    default boolean containsKey(int row, int column) {
        return this.containsKey(GridPoint.at(row, column));
    }
}
