package org.incendo.interfaces.paper.element;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;

/**
 * An element containing a piece of text.
 */
public class TextElement implements Element {

    private final @NonNull Component text;

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public TextElement(final @NonNull Component text) {
        this.text = text;
    }

    /**
     * Creates a new TextElement.
     *
     * @param text the text
     * @return the element
     */
    public static @NonNull TextElement of(final @NonNull Component text) {
        return new TextElement(text);
    }

    /**
     * Returns the element's text.
     *
     * @return the element's text
     */
    public @NonNull Component text() {
        return this.text;
    }


}
