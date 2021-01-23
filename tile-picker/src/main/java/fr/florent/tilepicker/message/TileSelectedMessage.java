package fr.florent.tilepicker.message;


import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.map.core.model.layer.TileLayer;

public class TileSelectedMessage extends AbstractMessage {
    private TileLayer layer;

    public TileSelectedMessage(TileLayer layer){
        super(TileSelectedMessage.class.getName());
        this.layer = layer;
    }

    public TileLayer getLayer() {
        return layer;
    }

}
