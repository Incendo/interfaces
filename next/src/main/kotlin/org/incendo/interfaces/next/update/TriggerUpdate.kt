package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.view.AbstractInterfaceView

public class TriggerUpdate(
    private val trigger: Trigger
) : Update {
    override fun <P : Pane> apply(target: AbstractInterfaceView<*, P>) {
        val filteredTransforms = target.backing.transforms
            .filter { transform -> transform.triggers.contains(trigger) }

        target.applyTransforms(filteredTransforms)
    }
}
