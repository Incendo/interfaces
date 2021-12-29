package org.incendo.interfaces.next.properties

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

public class InterfaceProperty<T>(
    defaultValue: T
) : ObservableProperty<T>(defaultValue), Trigger {

    override val listeners: MutableList<() -> Unit> = ArrayList()

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        for (listener in listeners) {
            listener()
        }
    }
}
