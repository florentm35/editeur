package fr.florent.model.editeur.tile;

import fr.florent.composant.cache.RessourceCache;
import fr.florent.model.editeur.IRessourceId;
import fr.florent.model.editeur.tileset.TileSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Tile extends AbstractTile {
    private Image cacheImage;
    private TileSet tileSet;


    // TODO : voir a déplacer dans une Factory, le modèle n'a pas à gérer le cache
    public Tile(TileSet tileSet, int x, int y) {

        this.id = tileSet.getId() + ";" + x + ";" + y;

        IRessourceId cacheRessource = RessourceCache.get(this);
        if (cacheRessource != null) {
            Tile tmp = (Tile) cacheRessource;
            this.cacheImage = tmp.getCacheImage();
            this.tileSet = tmp.getTileSet();
        } else {
            this.tileSet = tileSet;
            Image imagePng = tileSet.getImagePng();
            PixelReader reader = imagePng.getPixelReader();

            WritableImage newImage = new WritableImage(reader, x * tileSet.getTileWidth(), y * tileSet.getTileHeight(),
                    tileSet.getTileWidth(), tileSet.getTileHeight());
            cacheImage = newImage;

            RessourceCache.put(this);
        }
    }

    public Image getCacheImage() {
        return cacheImage;
    }

    public TileSet getTileSet() {
        return tileSet;
    }


}
