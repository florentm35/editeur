package fr.florent.map.core.model.tileset;

import javafx.scene.image.Image;

public class TileSet extends AbstractTileSet {
    private Image imagePng;

    private int tileWidth;
    private int tileHeight;

    private String name;

    public TileSet() {
    }

    public TileSet(String id) {
        this.id = id;
    }

    public TileSet(Image imagePng, int tileWidth, int tileHeight) {
        this.imagePng = imagePng;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Image getImagePng() {
        return imagePng;
    }

    public void setImagePng(Image imagePng) {
        this.imagePng = imagePng;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    @Override
    public String toString() {
        return "TileSet{" +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                ", id='" + id + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
