package fr.florent.map.core.model.tile;


import fr.florent.editor.core.cache.IRessourceId;
import fr.florent.editor.core.cache.ResourceCache;
import fr.florent.map.core.model.tileset.TileSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Tile extends AbstractTile {
    private Image cacheImage;
    private TileSet tileSet;


    // TODO : voir a déplacer dans une Factory, le modèle n'a pas à gérer le cache
    public Tile(TileSet tileSet, double x, double y) {

        this.id = tileSet.getId() + ";" + x + ";" + y;

        IRessourceId cacheRessource = ResourceCache.get(this);
        if (cacheRessource != null) {
            Tile tmp = (Tile) cacheRessource;
            this.cacheImage = tmp.getCacheImage();
            this.tileSet = tmp.getTileSet();
        } else {
            this.tileSet = tileSet;
            Image imagePng = tileSet.getImagePng();
            PixelReader reader = imagePng.getPixelReader();

            WritableImage newImage = new WritableImage(reader, (int) x * tileSet.getTileWidth(), (int) y * tileSet.getTileHeight(),
                    tileSet.getTileWidth(), tileSet.getTileHeight());
            cacheImage = newImage;

            ResourceCache.put(this);
        }
    }

    public Image getCacheImage() {
        return cacheImage;
    }

    public TileSet getTileSet() {
        return tileSet;
    }


}
