package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.view.InterfaceView;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PaperInventoryView extends InterfaceView, InventoryHolder {

    @NonNull Inventory inventory();

}
