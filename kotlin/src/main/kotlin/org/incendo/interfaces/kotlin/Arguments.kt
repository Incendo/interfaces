package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.arguments.ArgumentKey
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments
import org.incendo.interfaces.core.arguments.InterfaceArguments
import org.incendo.interfaces.core.arguments.MutableInterfaceArguments

/**
 * Returns a new [MutableInterfaceArguments] with the given [entries].
 *
 * @param entries the entries
 * @return the created argument
 */
public fun mutableInterfaceArgumentOf(vararg entries: ArgumentPair<*>): MutableInterfaceArguments {
    val builder = HashMapInterfaceArguments.Builder()

    entries.forEach { (key, value) ->
        // erase the type
        builder.with(ArgumentKey.of(key.key()), value as Any?)
    }

    return builder.build()
}

/**
 * Returns a new [InterfaceArguments] with the given [entries].
 *
 * @param entries the entries
 * @return the created argument
 */
public fun interfaceArgumentOf(vararg entries: ArgumentPair<*>): InterfaceArguments =
    mutableInterfaceArgumentOf(*entries).asImmutable()

/**
 * Creates a new [ArgumentKey] using the given type [T]
 *
 * @param key the key
 * @param T the type
 * @return the argument key
 */
public inline fun <reified T> argumentKeyOf(key: String): ArgumentKey<T> =
    ArgumentKey.of(key, T::class.java)

/**
 * Creates a new [ArgumentPair] using the given type T
 *
 * @param key the key
 * @param value the value
 * @param T the type
 * @return the argument pair
 */
public inline fun <reified T> argumentPairOf(key: String, value: T): ArgumentPair<T> =
    argumentKeyOf<T>(key) to value

public data class ArgumentPair<T>(public val key: ArgumentKey<T>, public val value: T)

public infix fun <T> ArgumentKey<T>.to(value: T): ArgumentPair<T> = ArgumentPair(this, value)
