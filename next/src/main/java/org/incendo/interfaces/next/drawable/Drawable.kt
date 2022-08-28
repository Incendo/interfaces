package org.incendo.interfaces.next.drawable

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

public fun interface Drawable {

    public companion object {
        public fun drawable(item: ItemStack): Drawable = Drawable { item }
    }

    public fun draw(player: Player): ItemStack
}
