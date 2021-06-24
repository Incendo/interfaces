package dev.kscott.interfaces.core.transform;

import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.core.view.InterfaceView;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface Transform<T extends Pane> extends BiFunction<T, InterfaceView, T> {

}
