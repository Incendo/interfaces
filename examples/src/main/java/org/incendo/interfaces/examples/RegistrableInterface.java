package org.incendo.interfaces.examples;

import org.incendo.interfaces.interfaces.Interface;

public interface RegistrableInterface {

    String subcommand();

    Interface<?> create();
}
