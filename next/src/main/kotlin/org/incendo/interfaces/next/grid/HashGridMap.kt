package org.incendo.interfaces.next.grid

public class HashGridMap<V> : GridMap<V> {
    private val backing: MutableMap<Int, MutableMap<Int, V>> = HashMap()

    override fun set(
        row: Int,
        column: Int,
        value: V,
    ) {
        val rowView = backing.computeIfAbsent(row) { HashMap() }
        rowView[column] = value
    }

    override fun get(
        row: Int,
        column: Int,
    ): V? {
        val rowView = backing[row] ?: return null
        return rowView[column]
    }

    override fun has(
        row: Int,
        column: Int,
    ): Boolean {
        val rowView = backing[row] ?: return false
        return rowView.containsKey(column)
    }

    override fun forEach(consumer: (Int, Int, V) -> Unit) {
        forEachInternal(consumer)
    }

    // todo(josh): investigate, workaround for kotlin suspend usage being terrible.
    override suspend fun forEachSuspending(consumer: suspend (Int, Int, V) -> Unit) {
        forEachInternal { row, column, value -> consumer(row, column, value) }
    }

    private inline fun forEachInternal(consumer: (Int, Int, V) -> Unit) {
        backing.forEach { (column, rowView) ->
            rowView.forEach { (row, value) ->
                consumer(column, row, value)
            }
        }
    }
}
