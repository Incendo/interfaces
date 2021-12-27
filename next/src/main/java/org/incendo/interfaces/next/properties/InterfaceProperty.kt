package org.incendo.interfaces.next.properties

import kotlin.properties.ReadWriteProperty

public sealed interface InterfaceProperty<T> : ReadWriteProperty<Any?, T> {

    public fun addListener(listener: (T, T) -> Unit)

    public fun forceRefresh()

}
