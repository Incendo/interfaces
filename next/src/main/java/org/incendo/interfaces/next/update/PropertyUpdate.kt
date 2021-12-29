package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.ListenableHolder
import org.incendo.interfaces.next.view.InterfaceView

public class PropertyUpdate(
    private val listenableHolder: ListenableHolder
) : Update {
    override fun <P : Pane> apply(target: InterfaceView<P>) {
        target.backing.transforms
            .filter { transform -> transform.listenableHolders().contains(listenableHolder) }
            .forEach(target::applyTransform)
    }
}
