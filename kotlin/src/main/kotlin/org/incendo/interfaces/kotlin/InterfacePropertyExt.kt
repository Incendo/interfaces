package org.incendo.interfaces.kotlin

import kotlin.reflect.KProperty
import org.incendo.interfaces.core.transform.InterfaceProperty

/** Setter delegate. */
public operator fun <T> InterfaceProperty<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) {
    this.set(value)
}

/** Getter delegate. */
public operator fun <T> InterfaceProperty<T>.getValue(thisRef: Any?, property: KProperty<*>): T =
    this.get()

/** Getter/Setter delegate. */
public var <T> InterfaceProperty<T>.value: T
    get() = this.get()
    set(value) {
        this.set(value)
    }
