package org.incendo.interfaces.next.properties

import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

public class InterfaceProperty<T>(
    defaultValue: T
) : ObservableProperty<T>(defaultValue), Trigger {

    private val listeners: MutableList<(T) -> Unit> = CopyOnWriteArrayList()

    public constructor(defaultValue: T, defaultListener: (T) -> Unit) : this(defaultValue) {
        listeners += defaultListener
    }

    public fun addListener(listener: (T) -> Unit) {
        listeners += listener
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        for (listener in listeners) {
            listener(newValue)
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
