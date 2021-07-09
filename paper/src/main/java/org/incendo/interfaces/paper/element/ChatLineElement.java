package org.incendo.interfaces.paper.element;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;

import java.util.List;

/**
 * An element representing a line of text comprised of .
 */
public class ChatLineElement implements Element {

    // The inner lists represent a single line per list.
    // The outer list represents all the lines to send.
    private final @NonNull List<List<TextElement>> textElements;

    public ChatLineElement(final @NonNull List<List<TextElement>> elements) {
        this.textElements = elements;
    }

}
