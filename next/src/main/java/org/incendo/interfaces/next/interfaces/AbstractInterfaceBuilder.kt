package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public abstract class AbstractInterfaceBuilder<P : Pane, I : Interface<P>>
internal constructor() : InterfaceBuilder<P, I>() {

    private val transformCounter by IncrementingInteger()

    protected val transforms: MutableCollection<AppliedTransform<P>> = mutableListOf()
    protected val clickPreprocessors: MutableCollection<ClickHandler> = mutableListOf()

    public fun withTransform(vararg triggers: Trigger, transform: Transform<P>) {
        transforms.add(AppliedTransform(transformCounter, triggers.toSet(), transform))
    }

    public fun addTransform(reactiveTransform: ReactiveTransform<P>) {
        transforms.add(AppliedTransform(transformCounter, reactiveTransform.triggers.toSet(), reactiveTransform))
    }

    public fun withPreprocessor(handler: ClickHandler) {
        clickPreprocessors += handler
    }
}
