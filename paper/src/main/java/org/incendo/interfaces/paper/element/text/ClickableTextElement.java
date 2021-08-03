package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.TextClickCause;
import org.incendo.interfaces.paper.element.ClickableElement;
import org.incendo.interfaces.paper.pane.TextPane;

import java.util.UUID;

/**
 * Represents a text element that can be clicked.
 */
public final class ClickableTextElement extends BaseTextElement implements ClickableElement<TextPane, TextClickCause,
        PlayerViewer> {

    private final @NonNull UUID uuid;
    private final @NonNull ClickHandler<TextPane, TextClickCause, PlayerViewer, ?> handler;

    /**
     * Constructs {@code TextElement}.
     *
     * @param text    the text
     * @param handler the click handler
     */
    public ClickableTextElement(
            final @NonNull Component text,
            final @NonNull ClickHandler<TextPane, TextClickCause, PlayerViewer, ?> handler
    ) {
        super(text);
        this.handler = handler;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Override
    public @NonNull ClickHandler<TextPane, TextClickCause, PlayerViewer,
            ? extends ClickContext<TextPane, TextClickCause, PlayerViewer>> clickHandler() {
        return this.handler;
    }

}
