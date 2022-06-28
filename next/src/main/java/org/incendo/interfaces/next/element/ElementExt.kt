package org.incendo.interfaces.next.element

import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.drawable.Drawable

public fun ItemStack.asElement(handler: ClickHandler = {}): Element = StaticElement(Drawable.item(this), handler)
