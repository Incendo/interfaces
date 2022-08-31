package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * An element containing a piece of text.
 */
public class ComponentElement implements TextElement {

    private final @NonNull Component text;

    /**
     * Constructs {@code TextElement}.
     *
     * @param text the text
     */
    public ComponentElement(final @NonNull Component text) {
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

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentElement that = (ComponentElement) o;
        return this.text.equals(that.text);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.text);
    }

}
