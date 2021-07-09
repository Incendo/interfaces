package org.incendo.interfaces.paper.element;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.type.Clickable;

/**
 * An element containing a piece of text.
 */
public class ClickableTextElement extends TextElement implements Clickable<ChatPane, InventoryClickEvent> {

    private final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane, InventoryClickEvent>> clickHandler;

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public ClickableTextElement(
            final @NonNull Component text,
            final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane,
                    InventoryClickEvent>> clickHandler
    ) {
        super(text);
        this.clickHandler = clickHandler;
    }

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public ClickableTextElement(final @NonNull Component text) {
        this(text, (ctx -> {
        }));
    }

    /**
     * Creates a new {@code ClickableTextElement}.
     *
     * @param text the text
     * @return the element
     */
    public static @NonNull ClickableTextElement of(final @NonNull Component text) {
        return new ClickableTextElement(text);
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
            final @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane,
                    InventoryClickEvent>> clickHandler
    ) {
        return new ClickableTextElement(text, clickHandler);
    }

    @Override
    public @NonNull ClickHandler<ChatPane, InventoryClickEvent, ? extends ClickContext<ChatPane, InventoryClickEvent>> clickHandler() {
        return this.clickHandler;
    }

}
