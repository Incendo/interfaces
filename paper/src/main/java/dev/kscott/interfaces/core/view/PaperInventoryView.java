package dev.kscott.interfaces.core.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PaperInventoryView extends InterfaceView, InventoryHolder {

    @NonNull Inventory inventory();

}
