package fr.florent.map.editor.controller.message;


import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.map.core.model.map.Map;

public class MapSaveMessage extends AbstractMessage {
    private Map map;

    public MapSaveMessage(Map map) {
        super(MapSaveMessage.class.getName());
        this.map = map;
    }


    public Map getMap() {
        return map;
    }


}
