package org.incendo.interfaces.paper.pane;

import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.element.TextElement;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A pane based off of a Minecraft book.
 */
public class BookPane implements Pane {

    private final @NonNull List<TextElement> pages;

    /**
     * Constructs {@code BookPane}.
     */
    public BookPane() {
        this.pages = new ArrayList<>();
    }

    /**
     * Constructs {@code BookPane}.
     *
     * @param pages the pages
     */
    public BookPane(final @NonNull List<TextElement> pages) {
        this.pages = pages;
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
     * @return a new {@code BookPane}
     */
    public @NonNull BookPane add(final @NonNull TextElement element) {
        final @NonNull List<TextElement> pages = new ArrayList<>(this.pages);
        pages.add(element);
        return new BookPane(pages);
    }

    /**
     * Inserts a page at the index.
     *
     * @param index   the index
     * @param element the element
     * @return a new {@code BookPane}
     * @throws IndexOutOfBoundsException if index is greater than the pane size or lesser than 0
     */
    public @NonNull BookPane add(final int index, final @NonNull TextElement element) {
        final @NonNull List<TextElement> pages = new ArrayList<>(this.pages);
        pages.add(index, element);
        return new BookPane(pages);
    }

    /**
     * Removes a page at the given index.
     *
     * @param index the index
     * @return a new {@code BookPane}
     * @throws IndexOutOfBoundsException if index is greater than the pane size or lesser than 0
     */
    public @NonNull BookPane remove(final int index) {
        final @NonNull List<TextElement> pages = new ArrayList<>(this.pages);
        pages.remove(index);
        return new BookPane(pages);
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
