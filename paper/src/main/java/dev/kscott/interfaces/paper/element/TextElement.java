package dev.kscott.interfaces.paper.element;

import dev.kscott.interfaces.core.element.Element;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An element containing a piece of text.
 */
public class TextElement implements Element {

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
