package fr.florent.model.selection;

import org.junit.Assert;
import org.junit.Test;

public class AreaHelperTest {


    @Test
    public void convertPointToGrid() {
        Point2D point = AreaHelper.convertPointToGrid(new Point2D(32, 32), 32, 32);

        Assert.assertEquals("X", 1, point.getX(), 0);
        Assert.assertEquals("Y", 1, point.getY(), 0);

    }

    @Test
    public void convertAreaToGrid() {

        Area arae = AreaHelper.convertAreaToGrid(new Area(new Point2D(32, 32), new Point2D(32, 32)), 32, 32);

        Assert.assertEquals("Begin X", 1, arae.getBegin().getX(), 0);
        Assert.assertEquals("Begin Y", 1, arae.getBegin().getY(), 0);
        Assert.assertEquals("End X", 1, arae.getEnd().getX(), 0);
        Assert.assertEquals("End Y", 1, arae.getEnd().getY(), 0);
    }

    @Test
    public void convertPointToScreen() {
        Point2D point = AreaHelper.convertPointToScreen(new Point2D(1, 1), 32, 32);

        Assert.assertEquals("X", 32, point.getX(), 0);
        Assert.assertEquals("Y", 32, point.getY(), 0);
    }

    @Test
    public void convertAreaToScreen() {

        Area arae = AreaHelper.convertAreaToScreen(new Area(new Point2D(1, 1), new Point2D(1, 1)), 32, 32);

        Assert.assertEquals("Begin X", 32, arae.getBegin().getX(), 0);
        Assert.assertEquals("Begin Y", 32, arae.getBegin().getY(), 0);
        Assert.assertEquals("End X", 32, arae.getEnd().getX(), 0);
        Assert.assertEquals("End Y", 32, arae.getEnd().getY(), 0);

    }


    @Test
    public void calculateAbsoluteAreaPos() {
        Area area = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(10, 10), new Point2D(15, 15))
        );

        Assert.assertEquals("area.getWidth()", 6, area.getWidth(), 0);
        Assert.assertEquals("area.getHeight()", 6, area.getHeight(), 0);
        Assert.assertEquals("area.getBegin().getX()", 10, area.getBegin().getX(), 0);
        Assert.assertEquals("area.getEnd().getX()", 15, area.getEnd().getX(), 0);
        Assert.assertEquals("area.getBegin().getY()", 10, area.getBegin().getY(), 0);
        Assert.assertEquals("area.getEnd().getY()", 15, area.getEnd().getY(), 0);
    }

    @Test
    public void calculateAbsoluteAreaPosWithPad() {
        Area area = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(10, 10), new Point2D(11, 11)),
                3, 3
        );

        Assert.assertEquals("area.getWidth()", 3, area.getWidth(), 0);
        Assert.assertEquals("area.getHeight()", 3, area.getHeight(), 0);
        Assert.assertEquals("area.getBegin().getX()", 10, area.getBegin().getX(), 0);
        Assert.assertEquals("area.getEnd().getX()", 12, area.getEnd().getX(), 0);
        Assert.assertEquals("area.getBegin().getY()", 10, area.getBegin().getY(), 0);
        Assert.assertEquals("area.getEnd().getY()", 12, area.getEnd().getY(), 0);
    }

    @Test
    public void calculateAbsoluteAreaNeg() {


        Area areaNeg = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(10, 10), new Point2D(5, 5))
        );

        Assert.assertEquals("areaNeg.getWidth()", 6, areaNeg.getWidth(), 0);
        Assert.assertEquals("areaNeg.getHeight()", 6, areaNeg.getHeight(), 0);
        Assert.assertEquals("areaNeg.getBegin().getX()", 5, areaNeg.getBegin().getX(), 0);
        Assert.assertEquals("areaNeg.getEnd().getX()", 10, areaNeg.getEnd().getX(), 0);
        Assert.assertEquals("areaNeg.getBegin().getY()", 5, areaNeg.getBegin().getY(), 0);
        Assert.assertEquals("areaNeg.getEnd().getY()", 10, areaNeg.getEnd().getY(), 0);
    }

    @Test
    public void calculateAbsoluteAreaNegWithPad() {


        Area areaNeg = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(11, 11), new Point2D(10, 10)),
                3, 3
        );

        Assert.assertEquals("areaNeg.getWidth()", 6, areaNeg.getWidth(), 0);
        Assert.assertEquals("areaNeg.getHeight()", 6, areaNeg.getHeight(), 0);
        Assert.assertEquals("areaNeg.getBegin().getX()", 8, areaNeg.getBegin().getX(), 0);
        Assert.assertEquals("areaNeg.getEnd().getX()", 13, areaNeg.getEnd().getX(), 0);
        Assert.assertEquals("areaNeg.getBegin().getY()", 8, areaNeg.getBegin().getY(), 0);
        Assert.assertEquals("areaNeg.getEnd().getY()", 13, areaNeg.getEnd().getY(), 0);
    }

    @Test
    public void calculateAbsoluteArea() {
        Area areaPos = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(10, 10), new Point2D(11, 11))
        );


        Assert.assertEquals("areaPos.getBegin().getX()", 10, areaPos.getBegin().getX(), 0);
        Assert.assertEquals("areaPos.getEnd().getX()", 11, areaPos.getEnd().getX(), 0);
        Assert.assertEquals("areaPos.getBegin().getY()", 10, areaPos.getBegin().getY(), 0);
        Assert.assertEquals("areaPos.getEnd().getY()", 11, areaPos.getEnd().getY(), 0);
        Assert.assertEquals("areaPos.getWidth()", 2, areaPos.getWidth(), 0);
        Assert.assertEquals("areaPos.getHeight()", 2, areaPos.getHeight(), 0);

    }

    @Test
    public void calculateAbsoluteAreaEndEqualBegin() {

        Area areaPos = AreaHelper.calculateAbsoluteArea(
                new Area(new Point2D(0, 0), new Point2D(0, 0))
        );

        Assert.assertEquals("areaPos.getBegin().getX()", 0, areaPos.getBegin().getX(), 0);
        Assert.assertEquals("areaPos.getEnd().getX()", 0, areaPos.getEnd().getX(), 0);
        Assert.assertEquals("areaPos.getBegin().getY()", 0, areaPos.getBegin().getY(), 0);
        Assert.assertEquals("areaPos.getEnd().getY()", 0, areaPos.getEnd().getY(), 0);
        Assert.assertEquals("areaPos.getWidth()", 1, areaPos.getWidth(), 0);
        Assert.assertEquals("areaPos.getHeight()", 1, areaPos.getHeight(), 0);

    }
}
