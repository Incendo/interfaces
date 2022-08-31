package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.TextClickCause;
import org.incendo.interfaces.paper.pane.TextPane;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TextElement extends Element {

    /**
     * Constructs a new text element
     *
     * @param text the text
     * @return the element
     */
    static @NonNull TextElement text(final @NonNull Component text) {
        return new ComponentElement(text);
    }

    /**
     * Constructs a new text element
     *
     * @param elements the elements
     * @return the element
     */
    static @NonNull TextElement text(final @NonNull TextElement... elements) {
        return new CombiningTextElement(List.of(elements));
    }

    /**
     * Constructs a new text element
     *
     * @param elements the elements
     * @return the element
     */
    static @NonNull TextElement text(final @NonNull List<TextElement> elements) {
        return new CombiningTextElement(elements);
    }

    /**
     * Constructs a new text element
     *
     * @param text    the text
     * @param handler the click handler
     * @return the element
     */
    static @NonNull ComponentElement text(
            final @NonNull Component text,
            final @Nullable ClickHandler<TextPane, TextClickCause, PlayerViewer, ClickContext<TextPane, TextClickCause,
                    PlayerViewer>> handler
    ) {
        return new ComponentElement(text, handler);
    }

    /**
     * Returns the component value of this element.
     *
     * @return the component value
     */
    @NonNull Component text();

    /**
     * Returns the click handler, if any.
     *
     * @return the {@link ClickHandler}, wrapped in an {@link Optional}
     */
    @NonNull Optional<@NonNull ClickHandler<TextPane, TextClickCause, PlayerViewer, ClickContext<TextPane, TextClickCause,
            PlayerViewer>>> clickHandler();

    /**
     * Returns the children of this text element.
     *
     * @return the children
     */
    @NonNull List<TextElement> children();

    /**
     * Returns the {@link UUID} of this element.
     *
     * @return the UUID
     */
    @NonNull UUID uuid();

}
