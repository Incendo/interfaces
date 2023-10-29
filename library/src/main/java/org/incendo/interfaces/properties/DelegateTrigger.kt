package org.incendo.interfaces.properties

import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

public open class DelegateTrigger : Trigger {

    private val updateListeners = ConcurrentHashMap.newKeySet<Pair<WeakReference<Any>, Any.() -> Unit>>()

    override fun trigger() {
        val iterator = updateListeners.iterator()
        while (iterator.hasNext()) {
            val (reference, consumer) = iterator.next()
            val obj = reference.get()
            if (obj == null) {
                iterator.remove()
                continue
            }
            obj.apply(consumer)
        }
    }

    override fun <T : Any> addListener(reference: T, listener: T.() -> Unit) {
        updateListeners.removeIf { it.first.get() == null }
        updateListeners.add(WeakReference(reference) as WeakReference<Any> to listener as (Any.() -> Unit))
    }
}
