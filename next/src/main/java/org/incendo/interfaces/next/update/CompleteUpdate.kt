package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane

public object CompleteUpdate : Update {

    override fun <P : Pane> apply(target: Interface<P>) {
        target.transforms.forEach { transform ->
            target.applyTransform(transform)
        }
    }
}
