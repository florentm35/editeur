package fr.florent.controller.editeur;

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
    private Rectangle rectangle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image imagePng = new Image(getClass().getResourceAsStream("/tileset.png"));
        this.tileSet = new TileSet(imagePng, 32, 32);

        initMap();
        renderMap();


        paneMap.setOnMouseMoved(e -> {
            if (tileSelected != null) {
                int column = (int) Math.floor(e.getX() / tileSet.getTileWidth());
                int line = (int) Math.floor(e.getY() / tileSet.getTileHeight());
                if (rectangle == null) {
                    rectangle = getRectangle(column, line);
                    boxImage.add(rectangle, column, line, tileSelected.getWidth(), tileSelected.getHeight());
                } else if (rectangle.getX() != column || rectangle.getY() != line) {
                    clearRectangle();
                    rectangle = getRectangle(column, line);
                    boxImage.add(rectangle, column, line, tileSelected.getWidth(), tileSelected.getHeight());
                }
            }


        });

        paneMap.setOnMouseExited(e -> clearRectangle());

    }

    /**
     * return Rectangle for selection of editeur
     *
     * @param column
     * @param line
     * @return
     */
    private Rectangle getRectangle(int column, int line) {

        int strockeSized = 2;

        Rectangle rectangle = new Rectangle();
        rectangle.setX(column);
        rectangle.setWidth(tileSelected.getWidth() * tileSet.getTileWidth() - strockeSized);
        rectangle.setY(line);
        rectangle.setHeight(tileSelected.getHeight() * tileSet.getTileHeight() - strockeSized);

        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(strockeSized);

        return rectangle;
    }

    /**
     * Remove the selection Rectangle
     */
    private void clearRectangle() {

        boxImage.getChildren().remove(this.rectangle);

        this.rectangle = null;

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
     * @param tileLayer
     */
    public void onChangeTileSelection(TileLayer tileLayer) {
        this.tileSelected = tileLayer;
        clearRectangle();
    }



}
