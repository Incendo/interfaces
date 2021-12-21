package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.utilities.LayeredGridMap

public interface Transform : (LayeredGridMap<Element>.LayerView) -> Unit {

}
