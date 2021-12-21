package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger
import org.incendo.interfaces.next.utilities.LayeredGridMap

public abstract class Interface<P : Pane>(pendingTransforms: Collection<Transform>) {

    private val transformCounter by IncrementingInteger()
    private val transforms = pendingTransforms.map { AppliedTransform(transformCounter, it) }

}
