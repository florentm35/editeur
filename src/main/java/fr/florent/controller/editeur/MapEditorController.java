package fr.florent.controller.editeur;

import fr.florent.composant.event.EventSelectionAndDragged;
import fr.florent.controller.AbstractController;
import fr.florent.model.editeur.layer.Layer;
import fr.florent.model.editeur.Map;
import fr.florent.model.editeur.layer.TileLayer;
import fr.florent.model.editeur.tile.Tile;
import fr.florent.model.editeur.tileset.TileSet;
import fr.florent.util.event.IActionDoubleIterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.net.URL;
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

    private int selectLayerId = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image imagePng = new Image(getClass().getResourceAsStream("/tileset.png"));
        this.tileSet = new TileSet(imagePng, 32, 32);


        initMap();
        renderMap();


        EventSelectionAndDragged eventDrag = new EventSelectionAndDragged(paneMap, map.getWidth() * tileSet.getTileWidth(), map.getHeight() * tileSet.getTileHeight());
        eventDrag.setOnRelease(a -> {


        });

        eventDrag.setOnDragged(area -> {

            int columnBegin = (int) Math.floor(area.getBegin().getX() / tileSet.getTileWidth());
            int lineBegin = (int) Math.floor(area.getBegin().getY() / tileSet.getTileHeight());

            int columnEnd = (int) Math.floor(area.getEnd().getX() / tileSet.getTileWidth());
            int lineEnd = (int) Math.floor(area.getEnd().getY() / tileSet.getTileHeight());

            int width = columnEnd - columnBegin;
            width += (width < 0 ? -tileSelected.getWidth() : tileSelected.getWidth());
            if (width < 0) {
                width +=1;
                width = (int) (Math.ceil(width / tileSelected.getWidth())) * tileSelected.getWidth();
                columnBegin += width;
                width = width * -1 + tileSelected.getWidth();
            } else {
                width = (int) (Math.floor(width / tileSelected.getWidth())) * tileSelected.getWidth();
            }

            int height = lineEnd - lineBegin;
            height += (height < 0 ? -tileSelected.getHeight() : tileSelected.getHeight());
            if (height < 0) {
                height+=1;
                height = (int) (Math.ceil(height / tileSelected.getHeight())) * tileSelected.getHeight();
                lineBegin += height;
                height = height * -1 + tileSelected.getHeight();
            } else {
                height = (int) (Math.floor(height / tileSelected.getHeight())) * tileSelected.getHeight();
            }


            updateSelectionMask(columnBegin, lineBegin, width, height);
        });

        eventDrag.setOnMouve((area, event) -> {
            if (tileSelected != null) {
                int column = (int) Math.floor(event.getX() / tileSet.getTileWidth());
                int line = (int) Math.floor(event.getY() / tileSet.getTileHeight());

                if (column + tileSelected.getWidth() > map.getWidth()) {
                    column = map.getWidth() - tileSelected.getWidth();
                }

                if (line + tileSelected.getHeight() > map.getHeight()) {
                    line = map.getHeight() - tileSelected.getHeight();
                }

                int width = tileSelected.getWidth();
                int height = tileSelected.getHeight();

                updateSelectionMask(column, line, width, height);
            }

        });

        paneMap.setOnMouseExited(e -> clearRectangle());

    }

    private void updateSelectionMask(int column, int line, int width, int height) {
        if (selectionMask == null) {
            selectionMask = getSelectionMask(column, line, width, height);
            boxImage.add(selectionMask, column, line, width, height);
        } else {

            double selectionWidth = (selectionMask.getWidth() + 2) / tileSet.getTileWidth();
            double selectionHeight = (selectionMask.getHeight() + 2) / tileSet.getTileHeight();

            if (selectionMask.getX() != column || selectionMask.getY() != line
                    || selectionWidth != width || selectionHeight != height) {
                clearRectangle();
                selectionMask = getSelectionMask(column, line, width, height);
                boxImage.add(selectionMask, column, line, width, height);
            }
        }
    }

    /**
     * return Rectangle for selection of editeur
     *
     * @param column
     * @param line
     * @return
     */
    private Rectangle getSelectionMask(int column, int line, int width, int height) {


        int strockeSized = 2;

        Rectangle rectangle = new Rectangle();
        rectangle.setX(column);
        rectangle.setWidth(width * tileSet.getTileWidth() - strockeSized);
        rectangle.setY(line);
        rectangle.setHeight(height * tileSet.getTileHeight() - strockeSized);

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
     * Create map for test
     */
    private void initMap() {
        this.map = new Map("test", 20, 20);


        final Layer<Tile> layer = map.addlayer(new TileLayer(map.getWidth(), map.getHeight()));
        cellIterate((i, j) -> {
            layer.put(new Tile(tileSet, 0, 0), i, j);
        });

        Layer<Tile> layer2 = map.addlayer(new TileLayer(map.getWidth(), map.getHeight()));
        cellIterate((i, j) -> {
            layer2.put(new Tile(tileSet, 1, 1), i, j);
        }, 2);

    }

    /**
     * Render map from the Layer of Map
     */
    private void renderMap() {
        cellIterate((i, j) -> {
            for (Layer layerMap : map.getLayers()) {
                if (layerMap instanceof TileLayer) {
                    TileLayer layer = (TileLayer) layerMap;
                    Tile tile = layer.get(i, j);
                    if (tile != null) {
                        ImageView node = new ImageView(tile.getCacheImage());
                        boxImage.add(node, i, j, 1, 1);
                    }
                }
            }
        });
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
