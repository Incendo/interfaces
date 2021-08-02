package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;

/**
 * Represents an element containing a piece of text as a {@link Component}.
 */
public interface TextElement extends Element {

    /**
     * Creates a new TextElement.
     *
     * @param text the text
     * @return the element
     */
    static @NonNull TextElement of(final @NonNull Component text) {
        return new BaseTextElement(text);
    }

    /**
     * Creates a new CombinedTextElement.
     *
     * @param text the text
     * @return the element
     */
    static @NonNull TextElement of(final @NonNull Component... text) {
        return new MultiTextElement(text);
    }


    /**
     * Returns the text of this element.
     *
     * @return the text
     */
    @NonNull Component text();

}
