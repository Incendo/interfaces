package org.incendo.interfaces.next.click

import java.util.concurrent.CompletableFuture

public fun interface AsynchronousClickHandler {

    public fun handleAsynchronous(context: ClickContext): CompletableFuture<Unit>

}


public fun interface SynchronousClickHandler : AsynchronousClickHandler {

    public companion object EMPTY : SynchronousClickHandler {
        override fun handleSynchronous(context: ClickContext): Unit = Unit
    }

    public fun handleSynchronous(context: ClickContext)

    override fun handleAsynchronous(context: ClickContext): CompletableFuture<Unit> {
        handleSynchronous(context)

        return CompletableFuture.completedFuture(Unit)
    }

}
