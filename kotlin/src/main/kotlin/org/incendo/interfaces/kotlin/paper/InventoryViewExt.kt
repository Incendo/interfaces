package org.incendo.interfaces.kotlin.paper

import org.bukkit.inventory.Inventory
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.paper.view.InventoryView

/**
 * Returns the inventory.
 *
 * @return the inventory
 */
public val <T : Pane> InventoryView<T>.inventory: Inventory
  get() = this.inventory()
