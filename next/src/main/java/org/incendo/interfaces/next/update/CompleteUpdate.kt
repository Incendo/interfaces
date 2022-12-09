package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.InterfaceView

public object CompleteUpdate : Update {

    override suspend fun <P : Pane> apply(target: InterfaceView<*, P>) {
        val transforms = target.backing.transforms
        target.applyTransforms(transforms)
    }
}
