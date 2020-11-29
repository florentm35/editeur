package fr.florent.tilepicker.message;

import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.map.core.model.tileset.TileSet;

public class TileSetCreateMessage extends AbstractMessage {

    private TileSet tileSet;

    public TileSetCreateMessage(TileSet tileSet) {
        super(TileSetCreateMessage.class.getName());
        this.tileSet = tileSet;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }
}
