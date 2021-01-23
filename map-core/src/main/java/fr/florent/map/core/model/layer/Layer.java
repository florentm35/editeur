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

    /**
     * Resize the Layer<br/>
     * Crope if new width or new height are higther of old width or old height else is lowest set null value
     *
     * @param width
     * @param height
     */
    public void resize(double width, double height) {

        Object[] tmp = new Object[(int) (width * height)];

        for (int x = 0; x < width && x < this.width; x++) {
            for (int y = 0; y < height && y < this.height; y++) {
                tmp[getIndex(x, y, width)] = tiles[getIndex(x, y)] ;
            }
        }

        this.width = width;
        this.height = height;
        this.tiles = tmp;
    }

    public void put(T obj, double x, double y) {
        tiles[getIndex(x, y)] = obj;
    }

    @SuppressWarnings("unchecked")
    public T get(double x, double y) {
        return (T) tiles[getIndex(x, y)];
    }

    private int getIndex(double x, double y) {
        return getIndex(x, y, width);
    }

    private int getIndex(double x, double y, double width) {
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
