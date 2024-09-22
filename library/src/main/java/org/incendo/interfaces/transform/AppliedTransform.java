package org.incendo.interfaces.transform;

import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Set;

public record AppliedTransform<P extends Pane>(
    int priority,
    Set<Trigger> triggers,
    Transform<P> transform,
    boolean async
) implements Transform<P> {

    @Override
    public void apply(final P pane, final InterfaceView view) {
        this.transform.apply(pane, view);
    }

}
