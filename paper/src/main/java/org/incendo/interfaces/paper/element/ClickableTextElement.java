package org.incendo.interfaces.paper.element;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.type.Clickable;

import java.util.UUID;

/**
 * An element containing a piece of text.
 */
public class ClickableTextElement extends TextElement implements Clickable<ChatPane, InventoryClickEvent> {

    private final @NonNull UUID uuid;
    private final @NonNull Component tooltip;
    private final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane, InventoryClickEvent>> clickHandler;

    /**
     * Constructs {@code TextElement}.
     *
     * @param text         the text
     * @param tooltip      the text hover tooltip
     * @param clickHandler the click handler
     */
    public ClickableTextElement(
            final @NonNull Component text,
            final @NonNull Component tooltip,
            final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane,
                    InventoryClickEvent>> clickHandler
    ) {
        super(text);

        this.clickHandler = clickHandler;
        this.tooltip = tooltip;

        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public ClickableTextElement(final @NonNull Component text) {
        this(text, Component.empty(), ctx -> {
        });
    }

    /**
     * Constructs {@code TextElement}.
     *
     * @param text    the text
     * @param tooltip the text hover tooltip
     */
    public ClickableTextElement(
            final @NonNull Component text,
            final @NonNull Component tooltip
    ) {
        this(text, tooltip, ctx -> {
        });
    }

    /**
     * Creates a new {@code ClickableTextElement}.
     *
     * @param text the text
     * @return the element
     */
    public static @NonNull ClickableTextElement of(final @NonNull Component text) {
        return of(text, Component.empty());
    }

    /**
     * Creates a new {@code ClickableTextElement}.
     *
     * @param text the text
     * @return the element
     */
    public static @NonNull ClickableTextElement of(final @NonNull Component text,
                                                   final @NonNull Component tooltip) {
        return of(text, tooltip, ClickHandler.cancel());
    }

    /**
     * Creates a new {@code ClickableTextElement}.
     *
     * @param text         the text
     * @param clickHandler the click handler
     * @return the element
     */
    public static @NonNull ClickableTextElement of(
            final @NonNull Component text,
            final @NonNull Component tooltip,
            final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane,
                    InventoryClickEvent>> clickHandler
    ) {
        return new ClickableTextElement(text, tooltip, clickHandler);
    }

    @Override
    public @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane, InventoryClickEvent>> clickHandler() {
        return this.clickHandler;
    }

    /**
     * Returns the tooltip text for this clickable element.
     *
     * @return the tooltip
     */
    public @NonNull Component tooltip() {
        return this.tooltip;
    }

    /**
     * Returns the UUID of this clickable element.
     *
     * @return the uuid
     */
    public @NonNull UUID uuid() {
        return this.uuid;
    }

}
