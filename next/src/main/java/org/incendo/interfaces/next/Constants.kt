package org.incendo.interfaces.next

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newFixedThreadPoolContext

internal object Constants {

    internal val SCOPE = CoroutineScope(newFixedThreadPoolContext(5, "interfaces"))
}
