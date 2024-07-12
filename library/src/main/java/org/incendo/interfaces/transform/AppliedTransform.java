package org.incendo.interfaces.transform;

import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Set;

public final class AppliedTransform<P extends Pane> implements Transform<P> {

    private final int priority;
    private final Set<Trigger> triggers;
    private final Transform<P> transform;

    public AppliedTransform(final int priority, final Set<Trigger> triggers, final Transform<P> transform) {
        this.priority = priority;
        this.triggers = triggers;
        this.transform = transform;
    }

    @Override
    public void apply(final P pane, final InterfaceView view) {
        this.transform.apply(pane, view);
    }

    public int priority() {
        return this.priority;
    }

    public Set<Trigger> triggers() {
        return this.triggers;
    }
}
