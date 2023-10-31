package org.incendo.interfaces.next.properties

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

public class InterfaceProperty<T>(
    defaultValue: T
) : ObservableProperty<T>(defaultValue), Trigger by DelegateTrigger() {

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        if (oldValue != newValue) {
            trigger()
        }
    }
}
