package org.incendo.interfaces.next.element

import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

public class StaticElement(
    private val itemStack: ItemStack,
    private val clickHandler: ClickHandler = {}
) : Element {

    override fun itemStack(): ItemStack = itemStack

    override fun clickHandler(): ClickHandler = clickHandler
}
