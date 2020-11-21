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
        if (this.getWidth() < 0) {
            return -this.getWidth();
        } else {
            return this.getWidth();
        }
    }

    /**
     * Return positive height
     *
     * @return
     */
    public double getAbsoluteHeight() {
        if (this.getHeight() < 0) {
            return -this.getHeight();
        } else {
            return this.getHeight();
        }
    }

    /**
     * Return min X between begin point and end point
     *
     * @return
     */
    public double getBeginAbsoluteX() {
        if (this.getWidth() < 0) {
            return this.getEnd().getX();
        } else {
            return this.getBegin().getX();
        }
    }

    /**
     * Return min Y between begin point and end point
     *
     * @return
     */
    public double getBeginAbsoluteY() {
        if (this.getHeight() < 0) {
            return this.getEnd().getY();
        } else {
            return this.getBegin().getY();
        }
    }

    /**
     * Return max X between begin point and end point
     *
     * @return
     */
    public double getEndAbsoluteX() {
        if (this.getWidth() < 0) {
            return this.getBegin().getX();
        } else {
            return this.getEnd().getX();
        }
    }

    /**
     * Return max Y between begin point and end point
     *
     * @return
     */
    public double getEndAbsoluteY() {
        if (this.getHeight() < 0) {
            return this.getBegin().getY();
        } else {
            return this.getEnd().getY();
        }
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
