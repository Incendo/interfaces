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
     * Constructs and returns a new ChatLineElememt with the provided elements.
     *
     * @param elements the elements
     * @return the line element
     */
    public static @NonNull ChatLineElement of(final @NonNull TextElement... elements) {
        return new ChatLineElement(List.of(elements));
    }

    /**
     * Returns the built {@link Component} representing this line of chat.
     *
     * @return the component
     */
    public @NonNull Component text() {
        final TextComponent.@NonNull Builder builder = Component.text();

        for (final @NonNull TextElement element : textElements) {
            if (element instanceof ClickableTextElement) {
                final @NonNull ClickableTextElement clickable = (ClickableTextElement) element;

                builder.append(
                        Component.text()
                                .append(clickable.text())
                                .clickEvent(ClickEvent.runCommand("/interfaces view_id handler_id"))
                                .hoverEvent(HoverEvent.showText(clickable.tooltip()))
                );
            } else {
                builder.append(element.text());
            }
        }

        return builder.asComponent();
    }

}
