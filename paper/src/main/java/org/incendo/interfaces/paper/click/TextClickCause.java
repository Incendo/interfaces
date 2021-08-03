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

    /**
     * Construcst {@code TextClickCause}.
     *
     * @param player  the player who clicked
     * @param element the element that was clicked
     */
    public TextClickCause(
            final @NonNull Player player,
            final @NonNull ClickableTextElement element
    ) {
        this.player = player;
        this.element = element;
    }

    /**
     * Returns the player
     *
     * @return the player
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * Returns the element
     *
     * @return the element
     */
    public @NonNull ClickableTextElement element() {
        return this.element;
    }

}
