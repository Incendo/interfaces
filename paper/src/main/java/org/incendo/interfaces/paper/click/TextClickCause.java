package org.incendo.interfaces.paper.click;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.element.text.ClickableTextElement;

/**
 * A click that was caused by clicking on a piece of text (i.e. book, chat)
 */
public class TextClickCause {

    private final @NonNull Player player;
    private final @NonNull ClickableTextElement element;

    public TextClickCause(final @NonNull Player player,
                          final @NonNull ClickableTextElement element) {
        this.player = player;
        this.element = element;
    }

    public @NonNull Player player() {
        return player;
    }

    public @NonNull ClickableTextElement element() {
        return element;
    }

}
