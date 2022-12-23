package org.incendo.interfaces.next.utilities

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

// todo(josh): recalculate value when max/min changed?
public class BoundInteger(
    initial: Int,
    public var min: Int,
    public var max: Int
) : ObservableProperty<Int>(initial) {

    override fun beforeChange(property: KProperty<*>, oldValue: Int, newValue: Int): Boolean {
        val acceptableRange = min..max

        if (newValue in acceptableRange) {
            return oldValue == newValue
        }

        val coercedValue = newValue.coerceIn(acceptableRange)
        var value by this

        value = coercedValue

        return false
    }
}
