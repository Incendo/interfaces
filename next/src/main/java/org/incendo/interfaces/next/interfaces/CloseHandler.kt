package org.incendo.interfaces.next.interfaces

import org.bukkit.event.inventory.InventoryCloseEvent
import org.incendo.interfaces.next.view.InterfaceView

public fun interface CloseHandler : suspend (InventoryCloseEvent.Reason, InterfaceView) -> Unit
