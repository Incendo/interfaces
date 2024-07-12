package org.incendo.interfaces.transform.builtin;

import org.incendo.interfaces.element.Element;
import org.incendo.interfaces.grid.GridPoint;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.view.InterfaceView;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class PaginationTransformation<P extends Pane> extends PagedTransformation<P> {

    private final Supplier<List<GridPoint>> positionGenerator;
    private List<Element> values;

    public PaginationTransformation(
        final Supplier<List<GridPoint>> positionGenerator,
        final List<Element> defaultValues,
        final PaginationButton back,
        final PaginationButton forward,
        final Trigger... extraTriggers
    ) {
        super(back, forward, extraTriggers);
        this.positionGenerator = positionGenerator;
        this.values = defaultValues;
        this.boundPage().max(this.maxPages());
    }

    @Override
    public void apply(final P pane, final InterfaceView view) {
        List<GridPoint> positions = this.positionGenerator.get();
        int slots = positions.size();
        int offset = this.page() * slots;

        IntStream.range(0, positions.size()).forEach(index -> {
            int currentIndex = index + offset;

            if (currentIndex < this.values.size()) {
                Element element = this.values.get(currentIndex);
                pane.put(positions.get(index), element);
            }
        });

        super.apply(pane, view);
    }

    private int maxPages() {
        int amount = this.values.size();
        int slotsPerPage = this.positionGenerator.get().size();
        double rawPages = (double) amount / slotsPerPage;

        // Round up the amount so we find the amount of pages needed
        // then we do - 1 because we zero-index the pages. Ensure the
        // value is at least 0 in case rawPages is 0.0.
        return Math.max((int) Math.ceil(rawPages) - 1, 0);
    }

    // Ensure to update the values and recalculate max pages when values change
    public void setValues(final List<Element> values) {
        this.values = values;
        this.boundPage().max(this.maxPages());
    }
}
