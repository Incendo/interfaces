package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.AbstractInterfaceView

public object CompleteUpdate : Update {

    override fun <P : Pane> apply(target: AbstractInterfaceView<*, P>) {
        val transforms = target.backing.transforms
        target.applyTransforms(transforms)
    }
}
