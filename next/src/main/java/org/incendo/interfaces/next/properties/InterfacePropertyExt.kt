package org.incendo.interfaces.next.properties

public fun <T> interfaceProperty(value: T): InterfaceProperty<T> = ValueInterfaceProperty(value)

public fun dummyInterfaceProperty(): InterfaceProperty<Nothing?> = DummyInterfaceProperty()
