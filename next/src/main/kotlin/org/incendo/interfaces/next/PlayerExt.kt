package org.incendo.interfaces.next

import org.bukkit.entity.Player
import org.incendo.interfaces.next.interfaces.Interface

public suspend fun Player.open(target: Interface<*>) {
    target.open(this)
}
