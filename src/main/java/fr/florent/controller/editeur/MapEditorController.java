package fr.florent.controller.editeur;

import fr.florent.composant.event.EventSelectionAndDragged;
import fr.florent.controller.AbstractController;
import fr.florent.model.editeur.Map;
import fr.florent.model.editeur.layer.Layer;
import fr.florent.model.editeur.layer.TileLayer;
import fr.florent.model.editeur.tile.Tile;
import fr.florent.model.editeur.tileset.TileSet;
import fr.florent.model.selection.Area;
import fr.florent.model.selection.AreaHelper;
import fr.florent.model.selection.Point2D;
import fr.florent.util.event.IActionDoubleIterator;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MapEditorController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MapEditorController.class.getName());

    public static final String RESSOURCE_VIEW_PATH = "/scene/mapEditor.fxml";
    public static final String RESSOURCE_TITLE = "Editor";

    public GridPane boxImage;
    public Pane paneMap;

    private Map map;
    private TileLayer tileSelected;
    private TileSet tileSet;
    private Rectangle selectionMask;

    private int selectLayerId = -1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image imagePng = new Image(getClass().getResourceAsStream("/tileset.png"));
        this.tileSet = new TileSet(imagePng, 32, 32);


        initMap();
        renderMap();
        initEvent();

    }

    private void initEvent() {
        EventSelectionAndDragged eventDrag = new EventSelectionAndDragged(paneMap, map.getWidth() * tileSet.getTileWidth(), map.getHeight() * tileSet.getTileHeight());
        eventDrag.setOnRelease(area -> {
            if (tileSelected != null && selectLayerId >= 0) {
                Area maskArea = new Area(new Point2D(area.getBegin().getX(), area.getBegin().getY()),
                        new Point2D(area.getEnd().getX(), area.getEnd().getY()));

                AreaHelper.convertAreaToGrid(maskArea, tileSet.getTileWidth(), tileSet.getTileHeight());

                AreaHelper.calculateAbsoluteArea(maskArea, tileSelected.getWidth(), tileSelected.getHeight());

                // Blit Tile
                Layer layer = map.getLayers().get(selectLayerId);

                if (layer instanceof TileLayer) {


                    TileLayer selection = tileSelected;
                    for (double x = maskArea.getBegin().getX(); x <= maskArea.getEnd().getX(); x++) {
                        for (double y = maskArea.getBegin().getY(); y <= maskArea.getEnd().getY(); y++) {

                            // Sauvegarde dans le layer
                            layer.put(selection.get((int) (x - maskArea.getBegin().getX()) % selection.getWidth(),
                                    (int) (y - maskArea.getBegin().getY()) % selection.getHeight()), (int) x, (int) y);

                            updateCellGrid((int) x, (int) y);
                        }
                    }
                }
            }

        });

        eventDrag.setOnDragged(area -> {
            if (tileSelected != null) {
                Area maskArea = new Area(new Point2D(area.getBegin().getX(), area.getBegin().getY()),
                        new Point2D(area.getEnd().getX(), area.getEnd().getY()));

                AreaHelper.convertAreaToGrid(maskArea, tileSet.getTileWidth(), tileSet.getTileHeight());

                AreaHelper.calculateAbsoluteArea(maskArea, tileSelected.getWidth(), tileSelected.getHeight());

                updateSelectionMask(maskArea);
            }
        });

        eventDrag.setOnMouve((area, event) -> {
            if (tileSelected != null) {
                double column = (int) Math.floor(event.getX() / tileSet.getTileWidth());
                double line = (int) Math.floor(event.getY() / tileSet.getTileHeight());

                if (column + tileSelected.getWidth() > map.getWidth()) {
                    column = map.getWidth() - tileSelected.getWidth();
                }

                if (line + tileSelected.getHeight() > map.getHeight()) {
                    line = map.getHeight() - tileSelected.getHeight();
                }

                double width = tileSelected.getWidth();
                double height = tileSelected.getHeight();

                Area maskArea = new Area(column, line, width, height);

                updateSelectionMask(maskArea);
            }

        });

        paneMap.setOnMouseExited(e -> clearRectangle());
    }

    /**
     * Create map for test
     */
    private void initMap() {
        this.map = new Map("test", 20, 20);


        final Layer<Tile> layer = map.addlayer(new TileLayer(map.getWidth(), map.getHeight()));
        cellIterate((i, j) -> {
            layer.put(new Tile(tileSet, 0, 0), i, j);
        });

        Layer<Tile> layer2 = map.addlayer(new TileLayer(map.getWidth(), map.getHeight()));

        selectLayerId = 1;

    }


    /**
     * Blit the slection mask if exist, if not, create it and blit it
     *
     * @param area
     */
    private void updateSelectionMask(Area area) {

        if (selectionMask == null) {
            selectionMask = getSelectionMask(area);


            boxImage.add(selectionMask, (int) area.getBegin().getX(), (int) area.getBegin().getY(),
                    (int) area.getWidth(), (int) area.getHeight());
        } else {

            double selectionWidth = (selectionMask.getWidth() + 2);
            double selectionHeight = (selectionMask.getHeight() + 2);

            if (selectionMask.getX() != area.getBegin().getX() || selectionMask.getY() != area.getBegin().getY()
                    || selectionWidth != area.getWidth() || selectionHeight != area.getHeight()) {
                clearRectangle();
                selectionMask = getSelectionMask(area);
                boxImage.add(selectionMask, (int) area.getBegin().getX(), (int) area.getBegin().getY(),
                        (int) area.getWidth(), (int) area.getHeight());
            }
        }
    }

    /**
     * return Rectangle for selection of editeur
     *
     * @param area
     * @return
     */
    private Rectangle getSelectionMask(Area area) {


        int strockeSized = 2;

        Rectangle rectangle = new Rectangle();
        rectangle.setX(area.getBegin().getX());

        rectangle.setWidth(area.getWidth() * tileSet.getTileWidth() - strockeSized);
        rectangle.setY(area.getBegin().getY());
        rectangle.setHeight(area.getHeight() * tileSet.getTileHeight() - strockeSized);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(strockeSized);


        return rectangle;
    }

    /**
     * Remove the selection Rectangle
     */
    private void clearRectangle() {

        boxImage.getChildren().remove(this.selectionMask);

        this.selectionMask = null;

    }


    /**
     * Render map from the Layer of Map
     */
    private void renderMap() {
        cellIterate((i, j) -> {
            updateCellGrid(i, j);
        });
    }

    private Rectangle getBorderCell() {
        Rectangle cell = new Rectangle();
        cell.setStroke(Color.GRAY);
        cell.setFill(Color.TRANSPARENT);
        cell.setWidth(tileSet.getTileWidth() - 1);
        cell.setHeight(tileSet.getTileHeight() - 1);
        cell.setStrokeWidth(0.5);
        return cell;
    }

    /**
     * Refresh map cell to given postion
     *
     * @param x
     * @param y
     */
    private void updateCellGrid(int x, int y) {

        removeNodeByRowColumnIndex(x, y, boxImage);

        for (Layer layerMap : map.getLayers()) {
            if (layerMap instanceof TileLayer) {
                TileLayer layer = (TileLayer) layerMap;
                Tile tile = layer.get(x, y);
                if (tile != null) {
                    ImageView node = new ImageView(tile.getCacheImage());
                    boxImage.add(node, x, y);
                }
            }
        }

        boxImage.add(getBorderCell(), x, y);
    }

    public void removeNodeByRowColumnIndex(final int column, final int row, GridPane gridPane) {

        ObservableList<Node> childrens = gridPane.getChildren();
        List<Node> copy = List.copyOf(childrens);

        for (Node node : copy) {
            if (node instanceof ImageView && gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                gridPane.getChildren().remove(node);
            }
        }
    }

    private void cellIterate(IActionDoubleIterator action) {
        cellIterate(action, 1);
    }

    /**
     * Iterate 0 to map.getWidth() and 0 to map.getHeight()<br/>
     * i = x<br/>
     * j = y
     *
     * @param action
     * @param increment
     */
    private void cellIterate(IActionDoubleIterator action, int increment) {
        for (int i = 0; i < map.getWidth(); i += increment) {
            for (int j = 0; j < map.getHeight(); j += increment) {

                action.action(i, j);
            }
        }
    }

    /**
     * Outer event call when tile selection change
     *
     * @param tileLayer
     */
    public void onChangeTileSelection(TileLayer tileLayer) {
        this.tileSelected = tileLayer;
        clearRectangle();
    }


}
