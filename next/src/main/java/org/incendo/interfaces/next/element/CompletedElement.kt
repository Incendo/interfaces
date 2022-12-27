package org.incendo.interfaces.next.element

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler

internal data class CompletedElement(
    public val itemStack: ItemStack,
    public val clickHandler: ClickHandler
)

internal suspend fun Element.complete(player: Player) = CompletedElement(
    drawable().draw(player),
    clickHandler()
)
