package org.incendo.interfaces.next.drawable

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

public fun interface Drawable {

    public companion object {
        public fun drawable(item: ItemStack): Drawable = Drawable { item }

        public fun drawable(material: Material): Drawable = Drawable { ItemStack(material) }
    }

    public fun draw(player: Player): ItemStack
}
