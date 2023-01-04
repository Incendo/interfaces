package org.incendo.interfaces.next.utilities

import org.bukkit.entity.Player
import org.incendo.interfaces.next.view.AbstractInterfaceView
import java.util.WeakHashMap

public open class PlayerDataMap : MutableMap<Player, AbstractInterfaceView<*, *>> by WeakHashMap()
