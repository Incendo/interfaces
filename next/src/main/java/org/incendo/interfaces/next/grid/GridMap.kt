package org.incendo.interfaces.next.grid

public interface GridMap<V> {

    public operator fun set(column: Int, row: Int, value: V)

    public operator fun set(vector: GridPoint, value: V) {
        set(vector.y, vector.x, value)
    }

    public operator fun get(column: Int, row: Int): V?

    public operator fun get(vector: GridPoint): V? {
        return get(vector.y, vector.x)
    }

    public fun has(column: Int, row: Int): Boolean

    public fun has(vector: GridPoint): Boolean {
        return has(vector.y, vector.x)
    }

    public fun forEach(consumer: (Int, Int, V) -> Unit)
}
