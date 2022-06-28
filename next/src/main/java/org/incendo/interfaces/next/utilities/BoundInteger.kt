package org.incendo.interfaces.next.utilities

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public class BoundInteger(
    initial: Int,
    public var min: Int,
    public var max: Int
) : ReadWriteProperty<Any, Int> {

    private var internal = initial

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = internal

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        internal = value.coerceIn(min, max)
    }
}
