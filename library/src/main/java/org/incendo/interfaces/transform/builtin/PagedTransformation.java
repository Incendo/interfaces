package org.incendo.interfaces.transform.builtin;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.incendo.interfaces.drawable.Drawable;
import org.incendo.interfaces.element.StaticElement;
import org.incendo.interfaces.grid.GridPoint;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.transform.ReactiveTransform;
import org.incendo.interfaces.utilities.BoundInteger;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Map;
import java.util.function.Consumer;

public abstract class PagedTransformation<P extends Pane> implements ReactiveTransform<P> {

    private final PaginationButton back;
    private final PaginationButton forward;
    private final Trigger[] extraTriggers;
    private final BoundInteger boundPage = new BoundInteger(0, 0, Integer.MAX_VALUE);
    private int page;

    public PagedTransformation(final PaginationButton back, final PaginationButton forward, final Trigger... extraTriggers) {
        this.back = back;
        this.forward = forward;
        this.extraTriggers = extraTriggers;
        this.page = this.boundPage.value();
    }

    public final BoundInteger boundPage() {
        return this.boundPage;
    }

    public final int page() {
        return this.page;
    }

    /**
     * Applies the transformation to the given pane and view.
     *
     * @param pane the pane to apply the transformation to
     * @param view the interface view
     */
    @Override
    public void apply(final P pane, final InterfaceView view) {
        if (this.boundPage.hasPreceding()) {
            this.applyButton(pane, this.back);
        }

        if (this.boundPage.hasSucceeding()) {
            this.applyButton(pane, this.forward);
        }
    }

    protected final void applyButton(final Pane pane, final PaginationButton button) {
        Map<ClickType, Integer> increments = button.increments();

        pane.put(button.position(), new StaticElement(button.drawable(), ($, context) -> {
            ClickType click = context.type();

            if (increments.containsKey(click)) {
                this.page += increments.get(click);
                this.boundPage.value(this.page);
            }

            button.clickHandler().accept(context.player());
        }));
    }

    @Override
    public final Trigger[] triggers() {
        return ArrayUtils.insert(0, this.extraTriggers, this.boundPage);
    }

    public record PaginationButton(
        GridPoint position,
        Drawable drawable,
        Map<ClickType, Integer> increments,
        Consumer<Player> clickHandler
    ) {
    }

}
