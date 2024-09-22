package org.incendo.interfaces.view;

import net.kyori.adventure.text.Component;

public interface InterfaceView {

    void open();

    void close();

    boolean isProcessingClick();

    void setProcessingClick(boolean processingClick);

    void title(Component value);
}
