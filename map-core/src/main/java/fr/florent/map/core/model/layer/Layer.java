package fr.florent.map.core.model.layer;

import java.util.Arrays;

public class Layer<T> {

    // TODO :  refactor double to double
    protected Object[] tiles;
    protected double width;
    protected double height;


    public Layer(double width, double height) {
        tiles = new Object[(int) (width * height)];
        this.width = width;
        this.height = height;
    }

    public void put(T obj, double x, double y) {
        tiles[getIndex(x, y)] = obj;
    }

    @SuppressWarnings("unchecked")
    public T get(double x, double y) {
        return (T) tiles[getIndex(x, y)];
    }

    private int getIndex(double x, double y) {
        return (int) (x + y * width);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
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
