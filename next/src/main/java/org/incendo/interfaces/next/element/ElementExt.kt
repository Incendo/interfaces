package org.incendo.interfaces.next.element

import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

public fun ItemStack.asElement(handler: ClickHandler = {}): Element = StaticElement(this, handler)

public operator fun Element.component1(): ItemStack = itemStack()

public operator fun Element.component2(): ClickHandler = clickHandler()
