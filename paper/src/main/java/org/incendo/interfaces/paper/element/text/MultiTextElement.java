package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An element containing a piece of text.
 */
public class MultiTextElement implements TextElement{

    private final @NonNull List<TextElement> elements;

    /**
     * Constructs {@code TextElement}.
     * <p>
     * Each {@link Component} within {@code elements} will be converted to a {@link ComponentElement}.
     *
     * @param elements the elements
     */
    public MultiTextElement(final @NonNull Component... elements) {
        this.elements = new ArrayList<>();

        for (final @NonNull Component element : elements) {
            this.elements.add(TextElement.of(element));
        }
    }

    /**
     * Returns the element's text.
     *
     * @return the element's text
     */
    public @NonNull Component text() {
        final TextComponent.@NonNull Builder text = Component.text();

        for (final @NonNull TextElement element : this.elements) {
            text.append(element.text());
        }

        return text.build();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiTextElement that = (MultiTextElement) o;

        return this.text().equals(that.text());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.text());
    }

}
