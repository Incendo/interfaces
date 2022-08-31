package org.incendo.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * Represents a titled interface. A title should never be null, rather returning an empty component if necessary.
 *
 * @param <T> the pane type
 * @param <U> the viewer type
 */
public interface ChildTitledInterface<T extends Pane, U extends InterfaceViewer> extends TitledInterface<T, U> {

    /**
     * Opens an interface with a parent view.
     *
     * @param parent      the parent view
     * @param arguments the interface's arguments
     * @param title  the title
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(
            @NonNull InterfaceView<?, U> parent,
            @NonNull InterfaceArguments arguments,
            @NonNull Component title
    );

}
