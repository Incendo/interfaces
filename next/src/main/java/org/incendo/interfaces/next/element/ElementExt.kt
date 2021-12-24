package org.incendo.interfaces.next.element

import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

public fun ItemStack.asElement(handler: ClickHandler): Element = StaticElement(this, handler)
