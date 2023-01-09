package org.incendo.interfaces.next.interfaces

public inline fun buildChestInterface(builder: ChestInterfaceBuilder.() -> Unit): ChestInterface =
    ChestInterfaceBuilder().also(builder).build()

public inline fun buildPlayerInterface(builder: PlayerInterfaceBuilder.() -> Unit): PlayerInterface =
    PlayerInterfaceBuilder().also(builder).build()

public inline fun buildCombinedInterface(builder: CombinedInterfaceBuilder.() -> Unit): CombinedInterface =
    CombinedInterfaceBuilder().also(builder).build()
