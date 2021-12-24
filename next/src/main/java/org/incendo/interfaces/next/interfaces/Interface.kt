package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.InterfaceView

public interface Interface<P : Pane> {

    public val transforms: Collection<AppliedTransform>

    public fun createPane(): P

    public fun open(): InterfaceView<P>
}
