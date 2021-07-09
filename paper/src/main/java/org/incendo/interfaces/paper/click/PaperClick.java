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

    /**
     * Returns the clicked slot number from the Bukkit API.
     *
     * @return the slot number
     * @see #rawSlot();
     */
    public int slot() {
        return this.event.getSlot();
    }

    /**
     * Returns the clicked slot's raw number.
     *
     * @return the raw slot number
     */
    public int rawSlot() {
        return this.event.getRawSlot();
    }

    /**
     * Returns the underlying {@link ClickType}.
     *
     * @return the click type
     */
    public @NonNull ClickType type() {
        return this.event.getClick();
    }

    /**
     * Returns the backing event for this click.
     *
     * @return the event
     */
    public @NonNull InventoryClickEvent event() {
        return this.event;
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
