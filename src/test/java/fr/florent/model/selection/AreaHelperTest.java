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

        Area arae = AreaHelper.convertAreaToGrid(new Area(new Point2D(32,32), new Point2D(32,32)), 32,32);

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

        Area arae = AreaHelper.convertAreaToScreen(new Area(new Point2D(1,1), new Point2D(1,1)), 32,32);

        Assert.assertEquals("Begin X", 32, arae.getBegin().getX(), 0);
        Assert.assertEquals("Begin Y", 32, arae.getBegin().getY(), 0);
        Assert.assertEquals("End X", 32, arae.getEnd().getX(), 0);
        Assert.assertEquals("End Y", 32, arae.getEnd().getY(), 0);

    }

}
