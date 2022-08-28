package org.incendo.interfaces.next.element

import org.incendo.interfaces.next.click.AsynchronousClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public interface Element {

    public fun drawable(): Drawable

    public fun clickHandler(): AsynchronousClickHandler
}
