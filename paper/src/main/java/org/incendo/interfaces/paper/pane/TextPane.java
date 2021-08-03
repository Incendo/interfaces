package org.incendo.interfaces.paper.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.element.text.TextElement;

import java.util.List;

/**
 * A pane containing text.
 */
public interface TextPane extends Pane {

    /**
     * Returns the list of elements.
     *
     * @return the list of elements
     */
    @NonNull List<TextElement> textElements();

}
