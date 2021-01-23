package fr.florent.map.editor.controller.message;


import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.map.core.model.layer.TileLayer;
import fr.florent.map.core.model.map.Map;

public class MapResizeMessage extends AbstractMessage {
    private Map map;

    public MapResizeMessage(Map map) {
        super(MapResizeMessage.class.getName());
        this.map = map;
    }


    public Map getMap() {
        return map;
    }


}
