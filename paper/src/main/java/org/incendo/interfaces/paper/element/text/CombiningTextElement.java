package org.incendo.interfaces.paper.element.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.PaperInterfaceListeners;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.TextClickCause;
import org.incendo.interfaces.paper.pane.TextPane;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code TextElement} is an element containing a piece of text and an optional click handler.
 */
public final class CombiningTextElement implements TextElement {

    private final @NonNull List<TextElement> elements;
    private final @Nullable ClickHandler<TextPane, TextClickCause, PlayerViewer, ClickContext<TextPane, TextClickCause,
            PlayerViewer>> handler;
    private final @NonNull UUID uuid;


    /**
     * Constructs {@code TextElement}.
     *
     * @param elements the elements
     */
    public CombiningTextElement(final @NonNull List<TextElement> elements) {
        this(elements, null);
    }

    /**
     * Constructs {@code TextElement}.
     *
     * @param elements the text
     * @param handler  the click handler
     */
    public CombiningTextElement(
            final @NonNull List<TextElement> elements,
            final @Nullable ClickHandler<TextPane, TextClickCause, PlayerViewer, ClickContext<TextPane, TextClickCause,
                    PlayerViewer>> handler
    ) {
        this.elements = elements;
        this.handler = handler;
        if (this.handler == null) {
            this.uuid = new UUID(0, 0);
        } else {
            this.uuid = UUID.randomUUID();
        }
    }

    /**
     * Returns the text contained within this element.
     * <p>
     * If this element has a non-null {@link ClickHandler},
     * then the {@link Component} returned by this method will have a {@link ClickEvent} applied.
     *
     * @return the text component
     */
    public @NonNull Component text() {
        if (this.handler != null) {
            return this.build()
                    .clickEvent(ClickEvent.runCommand(PaperInterfaceListeners.INTERFACES_COMMAND + " " + this.uuid));
        } else {
            return this.build();
        }
    }

    private @NonNull Component build() {
        final TextComponent.Builder builder = Component.text();
        for (final TextElement element : this.elements) {
            builder.append(element.text());
        }
        return builder.build();
    }

    /**
     * If there was a click handler provided to this element, it will be returned within the {@link Optional}.
     *
     * @return the click handler, wrapped in a {@link Optional}
     */
    public @NonNull Optional<ClickHandler<TextPane, TextClickCause, PlayerViewer, ClickContext<TextPane, TextClickCause,
            PlayerViewer>>> clickHandler() {
        return Optional.ofNullable(this.handler);
    }

    @Override
    public @NonNull List<TextElement> children() {
        return List.copyOf(this.elements);
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

}
