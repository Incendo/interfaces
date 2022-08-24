package org.incendo.interfaces.paper.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;
import org.incendo.interfaces.paper.utils.InterfacesUpdateExecutor;

public interface PaperInterface<T extends Pane, U extends InterfaceViewer> extends Interface<T, U> {

    /**
     * The executor in which to run update tasks on.
     * @return the executor
     */
    @NonNull InterfacesUpdateExecutor updateExecutor();

}
