package org.incendo.interfaces.element;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.drawable.Drawable;

public interface Element {

    Element EMPTY = new StaticElement(Drawable.drawable(Material.AIR));

    Drawable drawable();

    ClickHandler clickHandler();

    default CompletedElement complete(Player player) {
        return new CompletedElement(
            this.drawable().draw(player),
            this.clickHandler()
        );
    }

}
