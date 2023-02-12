package org.incendo.interfaces.next

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newFixedThreadPoolContext

internal object Constants {

    internal val SCOPE = CoroutineScope(SupervisorJob() + newFixedThreadPoolContext(5, "interfaces"))
}
