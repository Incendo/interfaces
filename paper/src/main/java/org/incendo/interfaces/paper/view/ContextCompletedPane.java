package org.incendo.interfaces.paper.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.transform.TransformContext;
import org.incendo.interfaces.paper.PlayerViewer;

final class ContextCompletedPane<P extends Pane> {

    private final TransformContext<?, P, PlayerViewer> context;
    private final P pane;

    ContextCompletedPane(
            final @NonNull TransformContext<?, P, PlayerViewer> context,
            final @NonNull P pane
    ) {
        this.context = context;
        this.pane = pane;
    }

    @NonNull TransformContext<?, P, PlayerViewer> context() {
        return this.context;
    }

    @NonNull P pane() {
        return this.pane;
    }

}
