package dev.kscott.interfaces.core.transform;

import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.core.view.InterfaceView;

import java.util.function.BiConsumer;

public interface Transform<T extends Pane> extends BiConsumer<T, InterfaceView> {

}
