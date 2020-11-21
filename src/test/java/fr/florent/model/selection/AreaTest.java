package fr.florent.model.selection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AreaTest {

    Area areaPos;
    Area areaNeg;

    @Before
    public void setUp(){
        areaPos= new Area(new Point2D(10,10), new Point2D(15,15));
        areaPos.calculateSize();
        areaNeg= new Area(new Point2D(10,10), new Point2D(5,5));
        areaNeg.calculateSize();

    }

    @Test
    public void getAbsoluteWidth() {

        Assert.assertEquals("getAbsoluteWidth: areaPos.getWidth()",5, areaPos.getWidth(), 0);
        Assert.assertEquals("getAbsoluteWidth: areaPos.getAbsoluteWidth()",5, areaPos.getAbsoluteWidth(), 0);

        Assert.assertEquals("getAbsoluteWidth: areaNeg.getWidth()",-5, areaNeg.getWidth(), 0);
        Assert.assertEquals("getAbsoluteWidth: areaNeg.getAbsoluteWidth()",5, areaNeg.getAbsoluteWidth(), 0);

    }


    @Test
    public void getAbsoluteHeight() {
        Assert.assertEquals("getAbsoluteWidth: areaPos.getHeight()",5, areaPos.getHeight(), 0);
        Assert.assertEquals("getAbsoluteWidth: areaPos.getAbsoluteHeight()",5, areaPos.getAbsoluteHeight(), 0);

        Assert.assertEquals("getAbsoluteWidth: areaNeg.getHeight()",-5, areaNeg.getHeight(), 0);
        Assert.assertEquals("getAbsoluteWidth: areaNeg.getAbsoluteHeight()",5, areaNeg.getAbsoluteHeight(), 0);
    }

    @Test
    public void getBeginAbsoluteX() {

        Assert.assertEquals("getBeginAbsoluteX: areaPos.getBegin().getX()",10, areaPos.getBegin().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaPos.getEnd().getX()",15, areaPos.getEnd().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaPos.getBeginAbsoluteX()",10, areaPos.getBeginAbsoluteX(), 0);

        Assert.assertEquals("getBeginAbsoluteX: areaNeg.getBegin().getX()",10, areaNeg.getBegin().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaNeg.getEnd().getX()",5, areaNeg.getEnd().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaNeg.getBeginAbsoluteX()",5, areaNeg.getBeginAbsoluteX(), 0);

    }

    @Test
    public void getBeginAbsoluteY() {
        Assert.assertEquals("getBeginAbsoluteY: areaPos.getBegin().getY()",10, areaPos.getBegin().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaPos.getEnd().getY()",15, areaPos.getEnd().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaPos.getBeginAbsoluteY()",10, areaPos.getBeginAbsoluteY(), 0);

        Assert.assertEquals("getBeginAbsoluteY: areaNeg.getBegin().getY()",10, areaNeg.getBegin().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaNeg.getEnd().getY()",5, areaNeg.getEnd().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaNeg.getBeginAbsoluteY()",5, areaNeg.getBeginAbsoluteY(), 0);

    }

    @Test
    public void getEndAbsoluteX() {
        Assert.assertEquals("getBeginAbsoluteX: areaPos.getBegin().getX()",10, areaPos.getBegin().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaPos.getEnd().getX()",15, areaPos.getEnd().getX(), 0);
        Assert.assertEquals("getEndAbsoluteX: areaPos.getEndAbsoluteX()",10, areaPos.getEndAbsoluteX(), 0);

        Assert.assertEquals("getBeginAbsoluteX: areaNeg.getBegin().getX()",10, areaNeg.getBegin().getX(), 0);
        Assert.assertEquals("getBeginAbsoluteX: areaNeg.getEnd().getX()",5, areaNeg.getEnd().getX(), 0);
        Assert.assertEquals("getEndAbsoluteX: areaNeg.getEndAbsoluteX()",5, areaNeg.getEndAbsoluteX(), 0);
    }

    @Test
    public void getEndAbsoluteY() {
        Assert.assertEquals("getBeginAbsoluteY: areaPos.getBegin().getY()",10, areaPos.getBegin().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaPos.getEnd().getY()",15, areaPos.getEnd().getY(), 0);
        Assert.assertEquals("getEndAbsoluteY: areaPos.getEndAbsoluteY()",10, areaPos.getEndAbsoluteY(), 0);

        Assert.assertEquals("getBeginAbsoluteY: areaNeg.getBegin().getY()",10, areaNeg.getBegin().getY(), 0);
        Assert.assertEquals("getBeginAbsoluteY: areaNeg.getEnd().getY()",5, areaNeg.getEnd().getY(), 0);
        Assert.assertEquals("getEndAbsoluteY: areaNeg.getEndAbsoluteY()",5, areaNeg.getEndAbsoluteY(), 0);
    }
}
