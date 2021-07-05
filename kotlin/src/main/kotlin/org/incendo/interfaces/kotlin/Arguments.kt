package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.arguments.HashMapInterfaceArgument
import org.incendo.interfaces.core.arguments.InterfaceArgument
import org.incendo.interfaces.core.arguments.MutableInterfaceArgument

/**
 * Returns a new [MutableInterfaceArgument] with the given [entries].
 *
 * @param entries the entries
 * @return the created argument
 */
public fun mutableInterfaceArgumentOf(vararg entries: Pair<String, Any>): MutableInterfaceArgument {
    val builder = HashMapInterfaceArgument.Builder()

    entries.forEach { (key, value) -> builder.with(key, value) }

    return builder.build()
}

/**
 * Returns a new [InterfaceArgument] with the given [entries].
 *
 * @param entries the entries
 * @return the created argument
 */
public fun interfaceArgumentOf(vararg entries: Pair<String, Any>): InterfaceArgument =
    mutableInterfaceArgumentOf(*entries).asImmutable()
