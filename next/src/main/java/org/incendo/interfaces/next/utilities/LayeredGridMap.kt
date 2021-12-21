package org.incendo.interfaces.next.utilities

public class LayeredGridMap<V> {

    private val layers = HashMap<Int, LayerView>()

    public operator fun get(layer: Int): LayerView = layers.computeIfAbsent(layer) { LayerView() }

    public inner class LayerView {
        private val backing = HashMap<Int, MutableMap<Int, V>>()

        public operator fun set(column: Int, row: Int, value: V) {
            val rowView = backing.computeIfAbsent(column) { HashMap() }

            rowView[row] = value
        }

        public operator fun set(vector: Vector2, value: V) {
            set(vector.y, vector.x, value)
        }

    }

}
