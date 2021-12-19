package org.incendo.interfaces.next.click

public typealias ClickHandler = (ClickContext) -> Unit

public fun cancelling(handler: ClickHandler): ClickHandler {
    return { context ->
        context.cancelled = true
        handler(context)
    }
}
