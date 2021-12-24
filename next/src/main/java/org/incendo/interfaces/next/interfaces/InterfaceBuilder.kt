package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.Pane

public abstract class InterfaceBuilder<P : Pane, T : Interface<P>> {

    public abstract fun build(): T
}
