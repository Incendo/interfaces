package org.incendo.interfaces.interfaces;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.transform.ReactiveTransform;
import org.incendo.interfaces.transform.Transform;
import org.incendo.interfaces.utilities.IncrementingInteger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class AbstractInterfaceBuilder<P extends Pane, I extends Interface<P>> implements InterfaceBuilder<P, I> {

    private static final List<InventoryCloseEvent.Reason> DEFAULT_REASONS = new ArrayList<>();

    static {
        for (InventoryCloseEvent.Reason reason : InventoryCloseEvent.Reason.values()) {
            if (reason != InventoryCloseEvent.Reason.PLUGIN) {
                DEFAULT_REASONS.add(reason);
            }
        }
    }

    private final IncrementingInteger transformCounter = new IncrementingInteger();

    private Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers = new HashMap<>();
    private Collection<AppliedTransform<P>> transforms = new ArrayList<>();
    private Collection<ClickHandler> clickPreprocessors = new ArrayList<>();

    public final Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers() {
        return this.closeHandlers;
    }

    public final void closeHandlers(final Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers) {
        this.closeHandlers = closeHandlers;
    }

    public final Collection<AppliedTransform<P>> transforms() {
        return this.transforms;
    }

    public final void transforms(final Collection<AppliedTransform<P>> transforms) {
        this.transforms = transforms;
    }

    public final Collection<ClickHandler> clickPreprocessors() {
        return this.clickPreprocessors;
    }

    public final void clickPreprocessors(final Collection<ClickHandler> clickPreprocessors) {
        this.clickPreprocessors = clickPreprocessors;
    }

    public final void withTransform(final Transform<P> transform, final Trigger... triggers) {
        this.transforms.add(new AppliedTransform<>(
            this.transformCounter.next(),
            new HashSet<>(List.of(triggers)),
            transform
        ));
    }

    public final void addTransform(final ReactiveTransform<P> reactiveTransform) {
        this.transforms.add(new AppliedTransform<>(
            this.transformCounter.next(),
            Set.of(reactiveTransform.triggers()),
            reactiveTransform
        ));
    }

    public final void withCloseHandler(final Collection<InventoryCloseEvent.Reason> reasons, final CloseHandler closeHandler) {
        for (InventoryCloseEvent.Reason reason : reasons) {
            this.closeHandlers.put(reason, closeHandler);
        }
    }

    public final void withCloseHandler(final CloseHandler closeHandler) {
        this.withCloseHandler(DEFAULT_REASONS, closeHandler);
    }

    public final void withPreprocessor(final ClickHandler handler) {
        this.clickPreprocessors.add(handler);
    }

}
