package fr.florent.map.core.model.map;

import fr.florent.map.core.model.layer.Layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map {
    private String title;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private ArrayList<Layer> layers;

    public Map(String title, int width, int height, int tileWidth, int tileHeight) {
        layers = new ArrayList<>();
        this.title = title;
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public <T> Layer<T> addlayer(Layer<T> layer) {
        layers.add(layer);
        return layer;
    }

    public <T> void remove(Layer<T> layer) {
        this.layers.remove(layer);
    }

    public List<Layer> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
