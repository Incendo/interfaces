package org.incendo.interfaces.paper.element;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;

import java.util.List;

/**
 * An element representing a line of text comprised of .
 */
public class ChatLineElement implements Element {

    // The inner lists represent a single line per list.
    // The outer list represents all the lines to send.
    private final @NonNull List<TextElement> textElements;

    public ChatLineElement(final @NonNull List<TextElement> elements) {
        this.textElements = elements;
    }

    /**
     * Constructs and returns a new ChatLineElement with the provided elements.
     *
     * @param elements the elements
     * @return the line element
     */
    public static @NonNull ChatLineElement of(final @NonNull TextElement... elements) {
        return new ChatLineElement(List.of(elements));
    }

    /**
     * Constructs and returns a new empty ChatLineElement.
     *
     * @return the line element
     */
    public static @NonNull ChatLineElement empty() {
        return new ChatLineElement(List.of());
    }

    /**
     * Constructs and returns a new ChatLineElement with the provided components.
     * <p>
     * This method will append all the components together, then that component to {@link #of(TextElement...)}.
     *
     * @param components the components
     * @return the line element
     */
    public static @NonNull ChatLineElement of(final @NonNull Component... components) {
        final TextComponent.@NonNull Builder builder = Component.text();

        for (final @NonNull Component component : components) {
            builder.append(component);
        }

        return new ChatLineElement(List.of(TextElement.of(builder.asComponent())));
    }

    /**
     * Returns all the text elements comprising this line.
     *
     * @return the text element list
     */
    public @NonNull List<TextElement> textElements() {
        return List.copyOf(this.textElements);
    }

}
