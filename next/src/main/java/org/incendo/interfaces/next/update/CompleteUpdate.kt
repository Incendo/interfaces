package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.Transform

public object CompleteUpdate : Update {

    override fun apply(pane: Pane, transforms: Collection<Transform>) {
        transforms.forEach { transform ->
            transform
        }
    }

}
