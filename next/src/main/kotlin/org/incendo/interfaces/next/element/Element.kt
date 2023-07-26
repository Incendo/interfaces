package org.incendo.interfaces.next.element

import org.bukkit.Material
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public interface Element {

    public companion object EMPTY : Element by StaticElement(Drawable.drawable(Material.AIR))

    public fun drawable(): Drawable

    public fun clickHandler(): ClickHandler
}
