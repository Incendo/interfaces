package org.incendo.interfaces.paper.element;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.Clickable;

/**
 * Holds an {@link ItemStack} in an element.
 *
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
     * @return an empty {@code ItemStackElement}.
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> empty() {
        return new ItemStackElement<T>(new ItemStack(Material.AIR));
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @return the element
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> of(final @NonNull ItemStack itemStack) {
        return new ItemStackElement<T>(itemStack);
    }

    /**
     * Returns an {@code ItemStackElement} with the provided ItemStack.
     *
     * @param itemStack the ItemStack
     * @param handler   the handler
     * @return the element
     */
    public static <T extends Pane> @NonNull ItemStackElement<T> of(
            final @NonNull ItemStack itemStack,
            final @NonNull ClickHandler<T> handler
    ) {
        return new ItemStackElement<T>(itemStack, handler);
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

}
