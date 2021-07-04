package org.incendo.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * Represents a titled interface. A title should never be null, rather returning an empty component if necessary.
 *
 * @param <T> the pane type
 * @param <U> the viewer type
 */
public interface TitledInterface<T extends Pane, U extends InterfaceViewer> extends Interface<T, U> {

    /**
     * Returns the title of this interface.
     *
     * @return the title
     */
    @NonNull Component title();

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer the viewer
     * @param title  the title
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(
            @NonNull U viewer,
            @NonNull Component title
    );

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @param title     the title
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(
            @NonNull U viewer,
            @NonNull InterfaceArgument arguments,
            @NonNull Component title
    );

}
