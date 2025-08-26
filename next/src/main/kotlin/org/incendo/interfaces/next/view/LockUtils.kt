package org.incendo.interfaces.next.view

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
internal inline fun <T> Lock.withLock(
    time: Long,
    unit: TimeUnit,
    action: () -> T,
): T? {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    return if (tryLock(time, unit)) {
        try {
            action()
        } finally {
            unlock()
        }
    } else {
        null
    }
}
