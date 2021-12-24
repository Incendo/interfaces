package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.InterfaceView

public object CompleteUpdate : Update {

    override fun <P : Pane> apply(target: InterfaceView<P>) {
        target.backing.transforms.forEach { transform ->
            target.applyTransform(transform)
        }
    }
}
