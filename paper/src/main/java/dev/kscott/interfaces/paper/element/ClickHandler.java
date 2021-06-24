package dev.kscott.interfaces.paper.element;

import dev.kscott.interfaces.paper.view.InventoryView;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public interface ClickHandler extends BiConsumer<InventoryClickEvent, InventoryView> {

}
