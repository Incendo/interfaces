package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.InterfaceView

public interface Interface<P : Pane> {

    public val initialTitle: Component?

    public val transforms: Collection<AppliedTransform>

    public val rows: Int

    public fun createPane(): P

    public fun open(player: Player): InterfaceView<P>
}
