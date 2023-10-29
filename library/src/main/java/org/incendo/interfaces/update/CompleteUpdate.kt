package org.incendo.interfaces.update

import kotlinx.coroutines.awaitAll
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.AbstractInterfaceView

public object CompleteUpdate : Update {

    override suspend fun <P : Pane> apply(target: AbstractInterfaceView<*, P>) {
        val transforms = target.backing.transforms
        target.applyTransforms(transforms).awaitAll()
        target.renderAndOpen(openIfClosed = true)
    }
}
