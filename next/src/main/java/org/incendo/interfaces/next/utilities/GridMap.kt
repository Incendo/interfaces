package org.incendo.interfaces.next.utilities

public interface GridMap<V> {

    public operator fun set(column: Int, row: Int, value: V)

    public operator fun set(vector: Vector2, value: V)

    public operator fun get(column: Int, row: Int): V?

    public operator fun get(vector: Vector2): V?

    public fun has(column: Int, row: Int): Boolean

    public fun has(vector: Vector2): Boolean

    public fun forEach(consumer: (Int, Int, V) -> Unit)
}
