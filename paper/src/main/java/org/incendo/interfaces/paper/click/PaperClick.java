package org.incendo.interfaces.paper.click;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;

/**
 * Holds data about a Paper {@link Click}.
 */
public class PaperClick implements Click  {

    private final @NonNull InventoryClickEvent event;

    /**
     * Constructs {@code PaperClick}.
     *
     * @param event the event
     */
    public PaperClick(final @NonNull InventoryClickEvent event) {
        this.event = event;
    }

    @Override
    public boolean rightClick() {
        return this.event.isRightClick();
    }

    @Override
    public boolean leftClick() {
        return this.event.isLeftClick();
    }

    @Override
    public boolean middleClick() {
        return this.event.getClick() == ClickType.MIDDLE;
    }

    @Override
    public boolean shiftClick() {
        return this.event.isShiftClick();
    }

}
