package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.click.SynchronousClickHandler
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public class ChestInterfaceBuilder internal constructor() : InterfaceBuilder<ChestPane, ChestInterface>() {

    public var rows: Int = 0
    public var initialTitle: Component? = null

    private val transformCounter by IncrementingInteger()
    private val transforms: MutableCollection<AppliedTransform<ChestPane>> = mutableListOf()

    private val clickPreprocessors: MutableCollection<SynchronousClickHandler> = mutableListOf()

    public fun withTransform(vararg triggers: Trigger, transform: Transform<ChestPane>) {
        transforms.add(AppliedTransform(transformCounter, triggers.toSet(), transform))
    }

    public fun addTransform(reactiveTransform: ReactiveTransform<ChestPane>) {
        transforms.add(AppliedTransform(transformCounter, reactiveTransform.triggers.toSet(), reactiveTransform))
    }

    public fun withPreprocessor(handler: SynchronousClickHandler) {
        clickPreprocessors += handler
    }

    public override fun build(): ChestInterface = ChestInterface(
        rows,
        initialTitle,
        transforms,
        clickPreprocessors
    )
}
