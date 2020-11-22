package fr.florent.controller.core.message;

import fr.florent.composant.message.AbstractMessage;

public class WindowsResizeMessage extends AbstractMessage {

    private double width;
    private double height;

    public WindowsResizeMessage(double width, double height) {
        this(WindowsResizeMessage.class.getName(), width, height);
    }

    public WindowsResizeMessage(String key, double width, double height) {
        super(key);

        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
