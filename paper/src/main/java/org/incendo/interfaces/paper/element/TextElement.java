package org.incendo.interfaces.paper.element;

import org.incendo.interfaces.core.element.Element;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An element containing a piece of text.
 */
public class TextElement implements Element {

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
     * This element's text.
     */
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
     * Returns the element's text.
     *
     * @return the element's text
     */
    public @NonNull Component text() {
        return this.text;
    }


}
