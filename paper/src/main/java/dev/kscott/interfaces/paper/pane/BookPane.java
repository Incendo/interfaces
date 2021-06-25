package dev.kscott.interfaces.paper.pane;

import dev.kscott.interfaces.core.element.Element;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.paper.element.TextElement;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A pane based off of a Minecraft book.
 */
public class BookPane implements Pane {

    /**
     * The list of elements containing text.
     */
    private final @NonNull List<TextElement> text;

    /**
     * Constructs {@code BookPane}.
     */
    public BookPane() {
        this.text = new ArrayList<>();
    }

    /**
     * Returns the list of text elements.
     *
     * @return the text elements
     */
    @Override
    public @NonNull Collection<Element> elements() {
        return List.copyOf(this.text);
    }

}
