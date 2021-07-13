package org.incendo.interfaces.core.transform;

import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.function.BiFunction;

public interface Transform<T extends Pane, U extends InterfaceViewer> extends BiFunction<T, InterfaceView<T, U>, T> {

}
