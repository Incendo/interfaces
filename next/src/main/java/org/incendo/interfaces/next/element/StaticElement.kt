package org.incendo.interfaces.next.element

import org.incendo.interfaces.next.click.AsynchronousClickHandler
import org.incendo.interfaces.next.click.SynchronousClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public class StaticElement private constructor(
    private val drawable: Drawable,
    private val clickHandler: AsynchronousClickHandler
) : Element {

    public companion object {
        public fun asyncHandler(
            drawable: Drawable,
            clickHandler: AsynchronousClickHandler = SynchronousClickHandler.EMPTY
        ): StaticElement {
            return StaticElement(drawable, clickHandler)
        }

        public fun syncHandler(
            drawable: Drawable,
            clickHandler: SynchronousClickHandler = SynchronousClickHandler.EMPTY
        ):
            StaticElement {
            return StaticElement(drawable, clickHandler)
        }
    }

    override fun drawable(): Drawable = drawable

    override fun clickHandler(): AsynchronousClickHandler = clickHandler
}
