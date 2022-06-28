package org.incendo.interfaces.next.element

import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public interface Element {

    public fun drawable(): Drawable

    public fun clickHandler(): ClickHandler
}
