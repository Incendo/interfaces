package org.incendo.interfaces.element;

import org.bukkit.inventory.ItemStack;
import org.incendo.interfaces.click.ClickHandler;

public record CompletedElement(
    ItemStack itemStack,
    ClickHandler clickHandler
) {
}
