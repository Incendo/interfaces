package org.incendo.interfaces.next.properties

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

internal class ValueInterfaceProperty<T>(
    defaultValue: T
) : ObservableProperty<T>(defaultValue), InterfaceProperty<T> {

    private val listeners: MutableList<(T, T) -> Unit> = ArrayList()

    override fun addListener(listener: (T, T) -> Unit) {
        listeners += listener
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        for (listener in listeners) {
            listener(oldValue, newValue)
        }
    }

}
