package dev.kscott.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a titled interface. A title should never be null, rather returning an empty component if necessary.
 */
public interface TitledInterface {

    /**
     * Returns the title of this interfface.
     *
     * @return the title
     */
    @NonNull Component title();

}
