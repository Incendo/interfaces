package org.incendo.interfaces.properties;

import it.unimi.dsi.fastutil.Pair;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

//todo(josh): must be a better way to do this
public class DelegateTrigger implements Trigger {

    private final Set<Pair<WeakReference<Object>, Consumer<Object>>> updateListeners = ConcurrentHashMap.newKeySet();

    @Override
    public final void trigger() {
        this.updateListeners.removeIf(pair -> pair.left().get() == null);
        this.updateListeners.forEach(pair -> {
            Object obj = pair.left().get();
            if (obj != null) {
                pair.right().accept(obj);
            }
        });
    }

    public final <T> void addListener(final T reference, final Consumer<T> listener) {
        this.updateListeners.add(Pair.of(new WeakReference<>(reference), (Consumer<Object>) listener));
    }

}
