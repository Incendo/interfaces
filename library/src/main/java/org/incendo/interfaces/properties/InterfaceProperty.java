package org.incendo.interfaces.properties;

public final class InterfaceProperty<T> extends DelegateTrigger {

    public static <T> InterfaceProperty<T> interfaceProperty(final T defaultValue) {
        return new InterfaceProperty<>(defaultValue);
    }

    private T value;

    public InterfaceProperty(final T defaultValue) {
        this.value = defaultValue;
    }

    public T value() {
        return this.value;
    }

    public void value(final T newValue) {
        T oldValue = this.value;
        this.value = newValue;

        if (oldValue != newValue) {
            this.trigger();
        }
    }
}
