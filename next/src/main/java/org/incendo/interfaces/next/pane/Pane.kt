package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.utilities.GridMap
import org.incendo.interfaces.next.utilities.HashGridMap

public open class Pane : GridMap<Element> by HashGridMap()
