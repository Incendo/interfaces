package org.incendo.interfaces.next.utilities

public class HashGridMap<V> : GridMap<V> {
    private val backing: MutableMap<Int, MutableMap<Int, V>> = HashMap()

    override fun set(column: Int, row: Int, value: V) {
        val rowView = backing.computeIfAbsent(column) { HashMap() }
        rowView[row] = value
    }

    override fun set(vector: GridPoint, value: V) {
        set(vector.y, vector.x, value)
    }

    override fun get(column: Int, row: Int): V? {
        val rowView = backing[column] ?: return null
        return rowView[row]
    }

    override fun get(vector: GridPoint): V? {
        return get(vector.y, vector.x)
    }

    override fun has(column: Int, row: Int): Boolean {
        val rowView = backing[column] ?: return false
        return rowView.containsKey(row)
    }

    override fun has(vector: GridPoint): Boolean {
        return has(vector.y, vector.x)
    }

    override fun forEach(consumer: (Int, Int, V) -> Unit) {
        backing.forEach { (column, rowView) ->
            rowView.forEach { (row, value) ->
                consumer(column, row, value)
            }
        }
    }
}
