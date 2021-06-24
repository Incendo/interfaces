package dev.kscott.interfaces.paper.element;

import dev.kscott.interfaces.core.element.Element;
import dev.kscott.interfaces.paper.pane.ChestPane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Holds an {@link ItemStack} in an element.
 *
 * @see ChestPane
 */
public class ItemStackElement implements Element {

    /**
     * The {@link ItemStack}.
     */
    private final @NonNull ItemStack itemStack;
    /**
     * The click handler.
     */
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

}
