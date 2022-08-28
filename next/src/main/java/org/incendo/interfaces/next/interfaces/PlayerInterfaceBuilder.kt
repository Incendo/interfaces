package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.click.SynchronousClickHandler
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.IncrementingInteger

public class PlayerInterfaceBuilder internal constructor() : InterfaceBuilder<PlayerPane, PlayerInterface>() {

    public var initialTitle: Component? = null

    private val transformCounter by IncrementingInteger()
    private val transforms: MutableCollection<AppliedTransform<PlayerPane>> = mutableListOf()

    private val clickPreprocessors: MutableCollection<SynchronousClickHandler> = mutableListOf()

    public fun withTransform(vararg triggers: Trigger, transform: Transform<PlayerPane>) {
        transforms.add(AppliedTransform(transformCounter, triggers.toSet(), transform))
    }

    public fun addTransform(reactiveTransform: ReactiveTransform<PlayerPane>) {
        transforms.add(AppliedTransform(transformCounter, reactiveTransform.triggers.toSet(), reactiveTransform))
    }

    public fun withPreprocessor(handler: SynchronousClickHandler) {
        clickPreprocessors += handler
    }

    public override fun build(): PlayerInterface = PlayerInterface(
        initialTitle,
        transforms,
        clickPreprocessors
    )
}
