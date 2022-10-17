package org.incendo.interfaces.paper.utils;

import com.google.gson.JsonElement;

import java.lang.reflect.Method;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class Components {

    private static final @Nullable Object NATIVE_GSON;
    private static final @Nullable Method NATIVE_DESERIALIZE;
    private static final String NOT_RELOCATED_PREFIX = String.join(".", "net", "kyori");

    static {
        if (!PaperUtils.isPaper()) {
            NATIVE_GSON = null;
            NATIVE_DESERIALIZE = null;
        } else {
            try {
                final Class<?> gsonClass = Class.forName(String.join(
                        ".",
                        "net",
                        "kyori",
                        "adventure",
                        "text",
                        "serializer",
                        "gson",
                        "GsonComponentSerializer"
                ));
                NATIVE_DESERIALIZE = gsonClass.getMethod("deserializeFromTree", JsonElement.class);
                NATIVE_GSON = gsonClass.getDeclaredMethod("gson").invoke(null);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private Components() {
    }

    /**
     * Convert the component to the native adventure type.
     *
     * @param component component
     * @return native adventure component
     */
    public static Object toNative(final Component component) {
        if (NATIVE_GSON == null || NATIVE_DESERIALIZE == null) {
            throw new IllegalStateException();
        }
        if (component.getClass().getName().startsWith(NOT_RELOCATED_PREFIX)) {
            return component;
        }
        try {
            return NATIVE_DESERIALIZE.invoke(NATIVE_GSON, GsonComponentSerializer.gson().serializeToTree(component));
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get the class object for the platform's native adventure.
     *
     * @return class
     */
    public static Class<?> nativeAdventureComponentClass() {
        try {
            return Class.forName(String.join(".", "net", "kyori", "adventure", "text", "Component"));
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

}
