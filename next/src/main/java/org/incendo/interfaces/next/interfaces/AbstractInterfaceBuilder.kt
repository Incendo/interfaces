package org.incendo.interfaces.next.interfaces

import org.bukkit.event.inventory.InventoryCloseEvent
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public abstract class AbstractInterfaceBuilder<P : Pane, I : Interface<P>> internal constructor() :
    InterfaceBuilder<P, I>() {

    private companion object {
        private val DEFAULT_REASONS = InventoryCloseEvent.Reason.values().toList().minus(InventoryCloseEvent.Reason.PLUGIN)
    }

    private val transformCounter by IncrementingInteger()

    protected val closeHandlers: MutableMap<InventoryCloseEvent.Reason, CloseHandler> = mutableMapOf()
    protected val transforms: MutableCollection<AppliedTransform<P>> = mutableListOf()
    protected val clickPreprocessors: MutableCollection<ClickHandler> = mutableListOf()

    public fun withTransform(vararg triggers: Trigger, transform: Transform<P>) {
        transforms.add(AppliedTransform(transformCounter, triggers.toSet(), transform))
    }

    public fun addTransform(reactiveTransform: ReactiveTransform<P>) {
        transforms.add(AppliedTransform(transformCounter, reactiveTransform.triggers.toSet(), reactiveTransform))
    }

    public fun withCloseHandler(
        reasons: Collection<InventoryCloseEvent.Reason> = DEFAULT_REASONS,
        closeHandler: CloseHandler
    ) {
        reasons.forEach {
            closeHandlers[it] = closeHandler
        }
    }

    public fun withPreprocessor(handler: ClickHandler) {
        clickPreprocessors += handler
    }
}
