package org.incendo.interfaces.kotlin.paper

import org.bukkit.inventory.Inventory
import org.incendo.interfaces.paper.view.PlayerView

/**
 * Returns the inventory.
 *
 * @return the inventory
 */
public val PlayerView<*>.inventory: Inventory
    get() = this.inventory
