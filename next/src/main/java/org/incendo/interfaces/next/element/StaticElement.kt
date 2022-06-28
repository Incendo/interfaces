package org.incendo.interfaces.next.element

import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public class StaticElement(
    private val drawable: Drawable,
    private val clickHandler: ClickHandler = {}
) : Element {

    override fun drawable(): Drawable = drawable

    override fun clickHandler(): ClickHandler = clickHandler
}
