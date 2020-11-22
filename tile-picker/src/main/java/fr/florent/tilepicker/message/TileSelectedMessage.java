package fr.florent.tilepicker.message;


import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.map.core.model.layer.TileLayer;

public class TileSelectedMessage extends AbstractMessage {
    private TileLayer layer;

    public TileSelectedMessage(){
        super(TileSelectedMessage.class.getName());
    }

    public TileLayer getLayer() {
        return layer;
    }

    public void setLayer(TileLayer layer) {
        this.layer = layer;
    }
}
