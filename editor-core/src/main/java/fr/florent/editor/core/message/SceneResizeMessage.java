package fr.florent.editor.core.message;

import fr.florent.editor.core.annotation.EnumScreenPosition;

public class SceneResizeMessage extends WindowsResizeMessage {

    public SceneResizeMessage(EnumScreenPosition position, double width, double height) {
        super(getKey(position), width, height);
    }

    public static String getKey(EnumScreenPosition position) {
        return String.format("%s;%s", position.name(), SceneResizeMessage.class.getName());
    }

}
