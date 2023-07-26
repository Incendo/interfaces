package org.incendo.interfaces.next.grid

public interface GridMap<V> {

    public operator fun set(row: Int, column: Int, value: V)

    public operator fun set(vector: GridPoint, value: V) {
        set(vector.x, vector.y, value)
    }

    public operator fun get(row: Int, column: Int): V?

    public operator fun get(vector: GridPoint): V? {
        return get(vector.x, vector.y)
    }

    public fun has(row: Int, column: Int): Boolean

    public fun has(vector: GridPoint): Boolean {
        return has(vector.x, vector.y)
    }

    public fun forEach(consumer: (row: Int, column: Int, V) -> Unit)

    public suspend fun forEachSuspending(consumer: suspend (row: Int, column: Int, V) -> Unit)
}
