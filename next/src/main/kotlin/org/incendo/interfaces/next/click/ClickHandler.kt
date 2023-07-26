package org.incendo.interfaces.next.click

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CompletionHandler

public fun interface ClickHandler {

    public companion object {
        public val EMPTY: ClickHandler = ClickHandler { }
        public val ALLOW: ClickHandler = ClickHandler { cancelled = false }

        public fun process(clickHandler: ClickHandler, context: ClickContext): Unit = with(clickHandler) {
            CompletableClickHandler().handle(context)
        }
    }

    public fun CompletableClickHandler.handle(context: ClickContext)
}

public class CompletableClickHandler {

    private val deferred = CompletableDeferred<Unit>(null)

    public var cancelled: Boolean = true
    public var completingLater: Boolean = false

    public fun complete(): Boolean = deferred.complete(Unit)

    public fun onComplete(handler: CompletionHandler): CompletableClickHandler {
        deferred.invokeOnCompletion(handler)
        return this
    }
}
