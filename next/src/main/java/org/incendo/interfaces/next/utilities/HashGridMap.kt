package org.incendo.interfaces.next.utilities

public class HashGridMap<V> : GridMap<V> {
    private val backing: MutableMap<Int, MutableMap<Int, V>> = HashMap()

    override fun set(column: Int, row: Int, value: V) {
        val rowView = backing.computeIfAbsent(column) { HashMap() }
        rowView[row] = value
    }

    override fun set(vector: Vector2, value: V) {
        set(vector.y, vector.x, value)
    }
}
