package fr.florent.controller.editeur.tilepicker.message;

import fr.florent.composant.message.AbstractMessage;
import fr.florent.model.editeur.layer.TileLayer;

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
