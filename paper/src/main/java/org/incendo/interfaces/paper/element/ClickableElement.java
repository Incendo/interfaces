package org.incendo.interfaces.paper.element;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.UUID;

public interface ClickableElement<T extends Pane, U, V extends InterfaceViewer> {

    @NonNull UUID uuid();

    @NonNull ClickHandler<T, U, V, ?> clickHandler();

}
