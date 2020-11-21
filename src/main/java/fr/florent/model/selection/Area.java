package fr.florent.model.selection;


public class Area {
    private Point2D begin;


    private Point2D end;
    private double width;
    private double height;

    public Area(double x, double y) {
        begin = new Point2D(x, y);
    }

    public Area(int x, int y, int width, int height) {
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

    /**
     * Return positive width
     *
     * @return
     */
    public double getAbsoluteWidth() {
        return Math.abs(this.getWidth());
    }

    /**
     * Return positive height
     *
     * @return
     */
    public double getAbsoluteHeight() {
        return Math.abs(this.getHeight());
    }

    /**
     * Return min X between begin point and end point
     *
     * @return
     */
    public double getBeginAbsoluteX() {
        return Math.min(this.getBegin().getX(), this.getEnd().getX());
    }

    /**
     * Return min Y between begin point and end point
     *
     * @return
     */
    public double getBeginAbsoluteY() {
        return Math.min(this.getBegin().getY(), this.getEnd().getY());
    }

    /**
     * Return max X between begin point and end point
     *
     * @return
     */
    public double getEndAbsoluteX() {
        return Math.max(this.getBegin().getX(), this.getEnd().getX());
    }

    /**
     * Return max Y between begin point and end point
     *
     * @return
     */
    public double getEndAbsoluteY() {
        return Math.max(this.getBegin().getY(), this.getEnd().getY());
    }

    /**
     * Update width and height
     */
    public void calculateSize() {
        this.setWidth(this.getEnd().getX() - this.getBegin().getX());
        this.setHeight(this.getEnd().getY() - this.getBegin().getY());
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
