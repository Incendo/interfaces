package org.incendo.interfaces.next.element

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

public interface Element {

    public fun itemStack(player: Player): ItemStack

    public fun clickHandler(): ClickHandler
}
