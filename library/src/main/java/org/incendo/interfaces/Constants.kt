package org.incendo.interfaces

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

internal object Constants {

    internal val SCOPE = CoroutineScope(
        CoroutineName("interfaces") +
            SupervisorJob() +
            run {
                val threadNumber = AtomicInteger()
                val factory = { runnable: Runnable ->
                    Thread("interfaces-${threadNumber.getAndIncrement()}").apply {
                        isDaemon = true
                    }
                }

                System.getProperty("org.incendo.interfaces.next.fixedPoolSize")
                    ?.toIntOrNull()
                    ?.coerceAtLeast(2)
                    ?.let { size ->
                        if (System.getProperty("org.incendo.interfaces.next.useScheduledPool").toBoolean()) {
                            Executors.newScheduledThreadPool(size, factory)
                        } else {
                            Executors.newFixedThreadPool(size, factory)
                        }
                    }
                    ?.asCoroutineDispatcher()
                    ?: Dispatchers.Default
            }
    )
}
