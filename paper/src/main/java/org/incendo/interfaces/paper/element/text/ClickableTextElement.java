package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.TextClickCause;
import org.incendo.interfaces.paper.pane.TextPane;
import org.incendo.interfaces.paper.type.Clickable;

public class ClickableTextElement extends BaseTextElement implements Clickable<TextPane, TextClickCause, PlayerViewer> {

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public ClickableTextElement(final @NonNull Component text) {
        super(text);
    }

    @Override
    public @NonNull ClickHandler<TextPane, TextClickCause, PlayerViewer, ? extends ClickContext<TextPane, TextClickCause, PlayerViewer>> clickHandler() {
        return null;
    }

}
