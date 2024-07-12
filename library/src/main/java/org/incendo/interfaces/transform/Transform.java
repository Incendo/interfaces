package org.incendo.interfaces.transform;

import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.view.InterfaceView;

@FunctionalInterface
public interface Transform<P extends Pane> {
    void apply(P pane, InterfaceView view);
}
