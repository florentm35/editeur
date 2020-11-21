package fr.florent.model.selection;

public class AreaHelper {

    public static Point2D convertPointToGrid(Point2D point, int tileWidth, int tileHeight) {
        point.setX(Math.floor(point.getX() / tileWidth));
        point.setY(Math.floor(point.getY() / tileHeight));
        return point;
    }

    public static Area convertAreaToGrid(Area area, int tileWidth, int tileHeight) {
        convertPointToGrid(area.getBegin(), tileWidth, tileHeight);
        convertPointToGrid(area.getEnd(), tileWidth, tileHeight);
        return area;
    }

    public static Point2D convertPointToScreen(Point2D point, int tileWidth, int tileHeight) {
        point.setX((int) point.getX() * tileWidth);
        point.setY((int) point.getY() * tileHeight);
        return point;
    }

    public static Area convertAreaToScreen(Area area, int tileWidth, int tileHeight) {
        convertPointToScreen(area.getBegin(), tileWidth, tileHeight);
        convertPointToScreen(area.getEnd(), tileWidth, tileHeight);
        return area;
    }

}
