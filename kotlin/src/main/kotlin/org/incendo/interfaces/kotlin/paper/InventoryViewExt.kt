package org.incendo.interfaces.kotlin.paper

import org.bukkit.inventory.Inventory
import org.incendo.interfaces.paper.view.InventoryView

/**
 * Returns the inventory.
 *
 * @return the inventory
 */
public val InventoryView<*>.inventory: Inventory
    get() = this.inventory()
