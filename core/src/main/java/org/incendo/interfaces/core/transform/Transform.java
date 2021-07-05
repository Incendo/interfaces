package org.incendo.interfaces.core.transform;

import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;

import java.util.function.BiFunction;

public interface Transform<T extends Pane> extends BiFunction<T, InterfaceView<T, ?>, T> {

}
