package fr.florent.model.selection;

import javafx.geometry.Point2D;

public class Area {
    private Point2D begin;
    private double width;
    private double height;

    public Area(double x, double y) {
        begin = new Point2D(x, y);
    }

    public Area(int x, int y, int width, int height) {
        begin = new Point2D(x, y);
        this.width = width;
        this.height = height;
    }

    public Point2D getBegin() {
        return begin;
    }

    public void setBegin(Point2D begin) {
        this.begin = begin;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAbsoluteX(){
        if (this.getWidth() < 0) {
            return this.getBegin().getX() + this.getWidth();
        }else{
            return this.getBegin().getX();
        }
    }
    public double getAbsoluteWidth(){
        if (this.getWidth() < 0) {
            return -this.getWidth();
        }else{
            return this.getWidth();
        }
    }

    public double getAbsoluteY(){
        if (this.getHeight() < 0) {
            return this.getBegin().getY() + this.getHeight();
        }else{
            return this.getBegin().getY();
        }
    }
    public double getAbsoluteHeight(){
        if (this.getHeight() < 0) {
            return -this.getHeight();
        }else{
            return this.getHeight();
        }
    }
    @Override
    public String toString() {
        return "Area{" +
                "begin=" + begin +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
