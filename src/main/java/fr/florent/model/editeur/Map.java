package fr.florent.model.editeur;

import fr.florent.model.editeur.layer.Layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map {
    private String title;
    private int width;
    private int height;
    private ArrayList<Layer> layers;

    public Map(String title, int width, int height){
        layers = new ArrayList<>();
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public <T> Layer<T> addlayer(Layer<T> layer){
        layers.add(layer);
        return layer;
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


}
