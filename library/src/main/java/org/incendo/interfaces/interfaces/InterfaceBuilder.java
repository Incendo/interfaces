package org.incendo.interfaces.interfaces;

import org.incendo.interfaces.pane.Pane;

public interface InterfaceBuilder<P extends Pane, I extends Interface<P>> {

    I build();

}
