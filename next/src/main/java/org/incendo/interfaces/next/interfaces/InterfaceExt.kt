package org.incendo.interfaces.next.interfaces

public fun buildChestInterface(builder: ChestInterfaceBuilder.() -> Unit): ChestInterface =
    ChestInterfaceBuilder().also(builder).build()

public fun buildPlayerInterface(builder: PlayerInterfaceBuilder.() -> Unit): PlayerInterface =
    PlayerInterfaceBuilder().also(builder).build()
