package fr.florent.map.editor.controller;


import fr.florent.editor.core.annotation.EnumScreenPosition;
import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.event.util.IActionDoubleIterator;
import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SceneResizeMessage;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.map.core.helper.AreaHelper;
import fr.florent.map.core.helper.EventSelectionAndDragged;
import fr.florent.map.core.model.Map;
import fr.florent.map.core.model.layer.Layer;
import fr.florent.map.core.model.layer.TileLayer;
import fr.florent.map.core.model.selection.Area;
import fr.florent.map.core.model.selection.Point2D;
import fr.florent.map.core.model.tile.Tile;
import fr.florent.map.core.model.tileset.TileSet;
import fr.florent.tilepicker.message.TileSelectedMessage;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Screen(value = EnumScreenPosition.CENTER, ressource = MapEditorController.RESSOURCE_VIEW_PATH)
public class MapEditorController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MapEditorController.class.getName());

    public static final String RESSOURCE_VIEW_PATH = "/map/editor/scene/mapEditor.fxml";
    public static final String RESSOURCE_TITLE = "Editor";

    public AnchorPane parent;


    public GridPane boxImage;
    public Pane paneMap;
    public ScrollPane scrollPane;

    private Map map;
    private TileLayer tileSelected;
    private TileSet tileSet;
    private Rectangle selectionMask;

    private int selectLayerId = -1;

    private boolean ctrlPressed;
    private double scale = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        parent.getStylesheets().add(ResourceLoader.getInstance().getRessource(this.getClass(), "/map/editor/css/mapEditor.css").toString());


        MessageSystem.getInstance().addObserver(TileSelectedMessage.class.getName(), this::onChangeTileSelection);

        Image imagePng = new Image(getClass().getResourceAsStream("/tileset.png"));
        this.tileSet = new TileSet(imagePng, 32, 32);
        initMap();
        renderMap();
        initEvent();

        initPaneMapEvent();


        MessageSystem.getInstance().addObserver(SceneResizeMessage.getKey(EnumScreenPosition.CENTER), this::onWindowsResize);

        paneMap.widthProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setMaxWidth(newVal.doubleValue() + 2);
        });

        paneMap.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setMaxHeight(newVal.doubleValue() + 2);
        });

    }

    private void initPaneMapEvent() {
        paneMap.setOnMouseEntered(e -> paneMap.requestFocus());

        paneMap.setOnKeyPressed(e -> {
            LOGGER.info("e.getCode() : " + e.getCode());
            if (e.getCode() == KeyCode.CONTROL) {
                ctrlPressed = true;
            }
        });

        paneMap.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.CONTROL) {
                ctrlPressed = false;
            }
        });

        paneMap.setOnScroll(e -> {
            LOGGER.info(String.format("%s : %b", "ctrlPressed", ctrlPressed));
            if (ctrlPressed) {

                scale += e.getDeltaY() > 0 ? 0.1 : -0.1;
                paneMap.getTransforms().clear();
                paneMap.getTransforms().add(new Scale(scale, scale));
            }
        });
    }

    public void onWindowsResize(AbstractMessage message) {
        if (message instanceof SceneResizeMessage) {
            SceneResizeMessage wMessage = (SceneResizeMessage) message;
            parent.setMaxWidth(wMessage.getWidth());
            parent.setMaxHeight(wMessage.getHeight());
        }
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
     * return Rectangle for fr.florent.map.core.selection of editeur
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
     * Remove the fr.florent.map.core.selection Rectangle
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
     * Outer event call when tile fr.florent.map.core.selection change
     *
     * @param message
     */
    public void onChangeTileSelection(AbstractMessage message) {
        if (message instanceof TileSelectedMessage) {
            this.tileSelected = ((TileSelectedMessage) message).getLayer();
            clearRectangle();
        }
    }


}
