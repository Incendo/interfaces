package org.incendo.interfaces.example.next

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

public fun ItemStack.name(name: String): ItemStack {
    itemMeta =
        itemMeta.also { meta ->
            meta.displayName(Component.text(name))
        }
    return this
}

public fun ItemStack.description(description: String): ItemStack {
    itemMeta =
        itemMeta.also { meta ->
            meta.lore(listOf(Component.text(description)))
        }
    return this
}
