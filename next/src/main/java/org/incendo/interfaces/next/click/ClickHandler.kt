package org.incendo.interfaces.next.click

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

public fun interface AsynchronousClickHandler {

    public fun handleAsynchronous(context: ClickContext): Deferred<Unit>
}

public fun interface SynchronousClickHandler : AsynchronousClickHandler {
    public companion object EMPTY : SynchronousClickHandler {
        override fun handleSynchronous(context: ClickContext): Unit = Unit
    }

    public fun handleSynchronous(context: ClickContext)

    override fun handleAsynchronous(context: ClickContext): Deferred<Unit> {
        handleSynchronous(context)

        return CompletableDeferred(Unit)
    }
}
