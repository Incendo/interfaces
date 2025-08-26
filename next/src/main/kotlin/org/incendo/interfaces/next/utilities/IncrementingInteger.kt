package org.incendo.interfaces.next.utilities

import kotlin.reflect.KProperty

internal class IncrementingInteger {
    private var value: Int = 0
        get() = field++

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Int = value
}
