package org.incendo.interfaces.next.utilities

public interface GridMap<V> {

    public operator fun set(column: Int, row: Int, value: V)

    public operator fun set(vector: Vector2, value: V)
}
