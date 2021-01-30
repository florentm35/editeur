package fr.florent.map.editor.controller.message;

import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;

public class OpenMapMessage extends AbstractMessage {
    public OpenMapMessage() {
        super(OpenMapMessage.class.getName());
    }
}
