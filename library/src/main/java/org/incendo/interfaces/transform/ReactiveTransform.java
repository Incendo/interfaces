package org.incendo.interfaces.transform;

import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;

public interface ReactiveTransform<P extends Pane> extends Transform<P> {

    Trigger[] triggers();

}
