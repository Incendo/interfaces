package org.incendo.interfaces.paper.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.TextPane;

import java.util.UUID;

/**
 * Represents a view of a {@link TextPane}.
 *
 * @param <T> the type of text pane
 */
public interface TextView<T extends TextPane> extends InterfaceView<T, PlayerViewer> {

    /**
     * Returns the UUID of this text view.
     *
     * @return view
     */
    @NonNull UUID uuid();

}
