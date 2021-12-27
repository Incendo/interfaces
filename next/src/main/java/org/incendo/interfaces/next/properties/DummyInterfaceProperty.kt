package org.incendo.interfaces.next.properties

import kotlin.reflect.KProperty

internal class DummyInterfaceProperty : InterfaceProperty<Nothing?> {

    private val listeners: MutableList<(Nothing?, Nothing?) -> Unit> = ArrayList()

    override fun addListener(listener: (Nothing?, Nothing?) -> Unit) {
        listeners += listener
    }

    override fun forceRefresh() {
        for (listener in listeners) {
            listener(null, null)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Nothing? {
        throw UnsupportedOperationException()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Nothing?) {
        throw UnsupportedOperationException()
    }

}
