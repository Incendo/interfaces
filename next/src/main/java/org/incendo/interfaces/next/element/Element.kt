package org.incendo.interfaces.next.element

import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

public interface Element {

    public fun itemStack(): ItemStack

    public fun clickHandler(): ClickHandler

}
