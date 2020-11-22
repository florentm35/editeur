package fr.florent.map.core.helper;

import fr.florent.map.core.model.selection.Area;
import fr.florent.map.core.model.selection.Point2D;

public class AreaHelper {

    public static Point2D convertPointToGrid(Point2D point, int tileWidth, int tileHeight) {
        point.setX(Math.floor(point.getX() / tileWidth));
        point.setY(Math.floor(point.getY() / tileHeight));
        return point;
    }

    public static Area convertAreaToGrid(Area area, int tileWidth, int tileHeight) {
        convertPointToGrid(area.getBegin(), tileWidth, tileHeight);
        convertPointToGrid(area.getEnd(), tileWidth, tileHeight);
        return calculateSize(area);
    }

    public static Point2D convertPointToScreen(Point2D point, int tileWidth, int tileHeight) {
        point.setX((int) point.getX() * tileWidth);
        point.setY((int) point.getY() * tileHeight);
        return point;
    }

    public static Area convertAreaToScreen(Area area, int tileWidth, int tileHeight) {
        convertPointToScreen(area.getBegin(), tileWidth, tileHeight);
        convertPointToScreen(area.getEnd(), tileWidth, tileHeight);
        return calculateSize(area);
    }


    public static Area calculateSize(Area area) {
        area.setWidth(area.getEnd().getX() - area.getBegin().getX() + 1);
        area.setHeight(area.getEnd().getY() - area.getBegin().getY() + 1);

        return area;
    }

    public static Area calculateAbsoluteArea(Area area) {
        return calculateAbsoluteArea(area, 1, 1);
    }

    public static Area calculateAbsoluteArea(Area area, double padWidth, double padHeight) {

        double beginX = area.getBegin().getX();
        double endX = area.getEnd().getX();

        double beginY = area.getBegin().getY();
        double endY = area.getEnd().getY();

        if (beginX > endX) {
            area.getBegin().setX(beginX - Math.ceil((beginX - endX) / padWidth) * padWidth);
            area.getEnd().setX(beginX + padWidth - 1);
        } else {

            //KO
            area.getEnd().setX(beginX + Math.ceil((endX + 1 - beginX ) / padWidth) * padWidth -1);
        }
        if (beginY > endY) {
            area.getBegin().setY(beginY - Math.ceil((beginY - endY) / padHeight) * padHeight);
            area.getEnd().setY(beginY + padHeight - 1);
        } else {
            //KO
            area.getEnd().setY(beginY + Math.ceil((endY + 1 - beginY) / padHeight) * padHeight -1) ;
        }

        return calculateSize(area);
    }


}
