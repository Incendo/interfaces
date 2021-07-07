package org.incendo.interfaces.paper.element;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.paper.pane.ChestPane;

import java.util.Objects;

/**
 * Holds an {@link ItemStack} in an element.
 *
 * @see ChestPane
 */
public class ItemStackElement implements Element {

    private final @NonNull ItemStack itemStack;
    private final @NonNull ClickHandler handler;

    /**
     * Constructs {@code ItemStackElement}.
     *
     * @param itemStack the {@link ItemStack}
     */
    public ItemStackElement(final @NonNull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.handler = (event, view) -> {
        };
    }

    /**
     * Constructs {@code ItemStackElement}.
     *
     * @param itemStack    the {@link ItemStack}
     * @param clickHandler the click handler
     */
    public ItemStackElement(
            final @NonNull ItemStack itemStack,
            final @NonNull ClickHandler clickHandler
    ) {
        this.itemStack = itemStack;
        this.handler = clickHandler;
    }

    /**
     * Returns an empty {@code ItemStackElement}.
     *
     * @return an empty {@code ItemStackElement}.
     */
    public static @NonNull ItemStackElement empty() {
        return new ItemStackElement(new ItemStack(Material.AIR));
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @return the element
     */
    public static @NonNull ItemStackElement of(final @NonNull ItemStack itemStack) {
        return new ItemStackElement(itemStack);
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @param handler   the handler
     * @return the element
     */
    public static @NonNull ItemStackElement of(
            final @NonNull ItemStack itemStack,
            final @NonNull ClickHandler handler
    ) {
        return new ItemStackElement(itemStack, handler);
    }

    /**
     * Returns this element's {@link ItemStack}.
     *
     * @return the {@link ItemStack}
     */
    public @NonNull ItemStack itemStack() {
        return this.itemStack;
    }

    /**
     * Returns the click handler.
     *
     * @return the click handler
     */
    public @NonNull ClickHandler handler() {
        return this.handler;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStackElement that = (ItemStackElement) o;
        return this.itemStack.equals(that.itemStack);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.itemStack);
    }

}
