package fr.florent.model.selection;


public class Area {
    private Point2D begin;


    private Point2D end;
    private double width;
    private double height;

    public Area(double x, double y) {
        begin = new Point2D(x, y);
    }

    public Area(double x, double y, double width, double height) {
        begin = new Point2D(x, y);
        end = new Point2D(x + width, y + height);
        this.width = width;
        this.height = height;
    }

    public Area(Point2D begin, Point2D end) {
        this.begin = begin;
        this.end = end;

        this.width = end.getX() - begin.getX();
        this.height = end.getY() - begin.getY();
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

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(Point2D end) {
        this.end = end;
    }


    @Override
    public String toString() {
        return "Area{" +
                "begin=" + begin +
                ", end=" + end +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
