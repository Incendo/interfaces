package org.incendo.interfaces.next.properties

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public class DelegatedInterfaceProperty<T, C : ObservableProperty<T>>(
    public val delegatedProperty: C
) : ReadWriteProperty<Any?, T> by delegatedProperty, Trigger {

    private val listeners: MutableList<(T) -> Unit> = ArrayList()

    public fun addListener(listener: (T) -> Unit) {
        listeners += listener
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val oldValue = getValue(thisRef, property)
        delegatedProperty.setValue(thisRef, property, value)

        val newValue = getValue(thisRef, property)

        if (oldValue != newValue) {
            this.afterChange(newValue)
        }
    }

    private fun afterChange(value: T) {
        for (listener in listeners) {
            listener(value)
        }
    }

    override fun trigger() {
        val value by this
        listeners.forEach { listener -> listener.invoke(value) }
    }

    override fun addListener(listener: () -> Unit) {
        listeners += { listener.invoke() }
    }
}
