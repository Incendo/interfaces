package org.incendo.interfaces.drawable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface Drawable {

    static Drawable drawable(ItemStack itemStack) {
        return player -> itemStack;
    }

    static Drawable drawable(Material material) {
        return player -> new ItemStack(material);
    }

    ItemStack draw(Player player);
}
