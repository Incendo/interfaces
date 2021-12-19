package org.incendo.interfaces.next.interfaces

public fun buildChestInterface(builder: ChestInterfaceBuilder.() -> Unit): ChestInterface =
    ChestInterfaceBuilder().also(builder).build()
