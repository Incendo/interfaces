package org.incendo.interfaces.paper.element;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.Clickable;

import java.util.Objects;

/**
 * Holds an {@link ItemStack} in an element.
 *
 * @param <T> the pane type
 * @see ChestPane
 */
public class ItemStackElement<T extends Pane> implements Element, Clickable<T> {

    private final @NonNull ItemStack itemStack;
    private final @NonNull ClickHandler<T> handler;

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
            final @NonNull ClickHandler<T> clickHandler
    ) {
        this.itemStack = itemStack;
        this.handler = clickHandler;
    }

    /**
     * Returns an empty {@code ItemStackElement}.
     *
     * @param <T> the pane type
     * @return an empty {@code ItemStackElement}.
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> empty() {
        return new ItemStackElement<>(new ItemStack(Material.AIR));
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @param <T> the pane type
     * @return the element
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> of(final @NonNull ItemStack itemStack) {
        return new ItemStackElement<>(itemStack);
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @param handler   the handler
     * @param <T> the pane type
     * @return the element
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> of(
            final @NonNull ItemStack itemStack,
            final @NonNull ClickHandler<T> handler
    ) {
        return new ItemStackElement<>(itemStack, handler);
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
    public @NonNull ClickHandler<T> clickHandler() {
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
