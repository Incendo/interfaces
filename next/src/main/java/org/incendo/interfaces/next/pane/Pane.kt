package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.grid.GridMap
import org.incendo.interfaces.next.grid.HashGridMap

public open class Pane : GridMap<Element> by HashGridMap()
