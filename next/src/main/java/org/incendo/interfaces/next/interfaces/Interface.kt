package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.Transform

public interface Interface<P : Pane> {

    public val transforms: Collection<Transform>

    public abstract class Builder<P : Pane, T : Interface<P>> {

        public abstract fun build(): T
    }
}
