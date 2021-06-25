package dev.kscott.interfaces.paper.pane;

import dev.kscott.interfaces.core.element.Element;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.paper.element.TextElement;
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
    private final @NonNull List<TextElement> pages;

    /**
     * Constructs {@code BookPane}.
     */
    public BookPane() {
        this.pages = new ArrayList<>();
    }

    /**
     * Returns the amount of lines in the pane.
     *
     * @return the amount of lines in the pane
     */
    public int count() {
        return this.pages.size();
    }

    /**
     * Adds a page.
     *
     * @param element the element
     */
    public void add(final @NonNull TextElement element) {
        this.pages.add(element);
    }

    /**
     * Inserts a page at the index.
     *
     * @param index   the index
     * @param element the element
     * @throws IndexOutOfBoundsException if index is greater than the pane size or lesser than 0
     */
    public void add(final int index, final @NonNull TextElement element) {
        this.pages.add(index, element);
    }

    /**
     * Removes a page at the given index.
     *
     * @param index the index
     * @throws IndexOutOfBoundsException if index is greater than the pane size or lesser than 0
     */
    public void remove(final int index) {
        this.pages.remove(index);
    }

    /**
     * Returns the list of text elements.
     *
     * @return the text elements
     */
    @Override
    public @NonNull Collection<Element> elements() {
        return List.copyOf(this.pages);
    }

    /**
     * Returns the list of pages.
     *
     * @return the list of pages
     */
    public @NonNull List<TextElement> pages() {
        return List.copyOf(this.pages);
    }

}
