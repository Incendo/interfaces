package org.incendo.interfaces.next.interfaces

import org.bukkit.entity.Player
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.utilities.currentOpenInterface
import org.incendo.interfaces.next.view.InterfaceView

public interface Interface<P : Pane> {

    public val rows: Int

    public val transforms: Collection<AppliedTransform<P>>

    public val clickPreprocessors: Collection<ClickHandler>

    public fun createPane(): P

    /**
     * Opens an [InterfaceView] from this [Interface].
     *
     * @param player the player to show the view
     * @param parent the parent view that is opening the interface, if null the view will be calculated by interfaces
     * @return the view
     */
    public suspend fun open(player: Player, parent: InterfaceView? = currentOpenInterface(player)): InterfaceView
}
