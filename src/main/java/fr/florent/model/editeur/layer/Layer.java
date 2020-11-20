package fr.florent.model.editeur.layer;

import java.util.Arrays;

public class Layer<T> {

    protected Object[] tiles;
    protected int width;
    protected int height;


    public Layer(int width, int height) {
        tiles = new Object[width * height];
        this.width = width;
        this.height = height;
    }

    public void put(T obj, int x, int y) {
        tiles[getIndex(x, y)] = obj;
    }

    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        return (T) tiles[getIndex(x, y)];
    }

    private int getIndex(int x, int y) {
        return x + y * width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "tiles=" + Arrays.toString(tiles) +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

}
