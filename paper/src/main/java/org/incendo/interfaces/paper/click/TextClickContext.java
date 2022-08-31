package org.incendo.interfaces.paper.click;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.clicks.Clicks;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.TextPane;

/**
 * {@code TextClickContext} holds context information for a click on a
 * {@link org.incendo.interfaces.paper.element.text.TextElement}.
 *
 * @param <T> the pane type
 * @param <U> the view type
 */
public final class TextClickContext<T extends TextPane, U extends InterfaceView<T, PlayerViewer>> implements
        ClickContext<T, TextClickCause, PlayerViewer> {

    private @NonNull ClickStatus clickStatus;
    private final @NonNull TextClickCause cause;
    private final @NonNull U view;
    private final @NonNull PlayerViewer viewer;
    private final @NonNull Click<TextClickCause> click;

    /**
     * Constructs {@code TextClickContext}.
     *
     * @param cause  the cause
     * @param viewer the viewer
     * @param view   the view
     */
    public TextClickContext(
            final @NonNull TextClickCause cause,
            final @NonNull PlayerViewer viewer,
            final @NonNull U view
    ) {
        this.cause = cause;
        this.click = Clicks.leftClick(
                cause,
                false,
                false
        );
        this.viewer = viewer;
        this.view = view;
        this.clickStatus = ClickStatus.ALLOW;
    }

    @Override
    public @NonNull TextClickCause cause() {
        return this.cause;
    }

    @Override
    public @NonNull ClickStatus status() {
        return this.clickStatus;
    }

    @Override
    public void status(@NonNull final ClickStatus status) {
        this.clickStatus = status;
    }

    @Override
    public @NonNull InterfaceView<T, PlayerViewer> view() {
        return this.view;
    }

    @Override
    public @NonNull PlayerViewer viewer() {
        return this.viewer;
    }

    @Override
    public @NonNull Click<TextClickCause> click() {
        return this.click;
    }

}
