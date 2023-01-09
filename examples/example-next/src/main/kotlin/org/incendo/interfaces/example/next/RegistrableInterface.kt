package org.incendo.interfaces.example.next

import org.incendo.interfaces.next.interfaces.Interface

public interface RegistrableInterface {

    public val subcommand: String

    public fun create(): Interface<*>
}
