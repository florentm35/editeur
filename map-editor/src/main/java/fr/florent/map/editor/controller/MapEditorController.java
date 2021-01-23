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
import fr.florent.map.core.model.map.Map;
import fr.florent.map.core.model.layer.Layer;
import fr.florent.map.core.model.layer.TileLayer;
import fr.florent.map.core.model.map.MapHelper;
import fr.florent.map.core.model.selection.Area;
import fr.florent.map.core.model.selection.Point2D;
import fr.florent.map.core.model.tile.Tile;
import fr.florent.map.editor.controller.message.MapResizeMessage;
import fr.florent.tilepicker.TilePickerController;
import fr.florent.tilepicker.message.TileSelectedMessage;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//TODO : refactor constante string (label, xml path, css path, ...)
@Screen(value = EnumScreenPosition.CENTER, ressource = MapEditorController.RESSOURCE_VIEW_PATH)
public class MapEditorController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MapEditorController.class.getName());

    public static final String RESSOURCE_VIEW_PATH = "/map/editor/scene/mapEditor.fxml";
    public static final String RESSOURCE_TITLE = "Editor";

    public static final String ID_TAB_LAYER_ADD = "tabLayer+";

    public AnchorPane parent;

    public TabPane tabPane;
    public MenuButton mapParamButton;

    public GridPane boxImage;
    public Pane paneMap;
    public ScrollPane scrollPane;

    private Map map;
    private TileLayer tileSelected;
    private Rectangle selectionMask;

    private int selectLayerId = -1;

    private boolean ctrlPressed;
    private double scale = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        parent.getStylesheets().add(ResourceLoader.getInstance().getRessource(this.getClass(), "/map/editor/css/mapEditor.css").toString());

        MessageSystem.getInstance().addObserver(TileSelectedMessage.class.getName(), this::onChangeTileSelection);

        initMap();
        initTabPaneLayer();
        renderMap();
        initEvent();

        initPaneMapEvent();

        initMapSettings();


        MessageSystem.getInstance().addObserver(SceneResizeMessage.getKey(EnumScreenPosition.CENTER), this::onWindowsResize);

        MessageSystem.getInstance().addObserver(MapResizeMessage.class.getName(), this::onMapResize);


        paneMap.widthProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setMaxWidth(newVal.doubleValue() + 2);
        });

        paneMap.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setMaxHeight(newVal.doubleValue() + 2);
        });

    }

    private void initMapSettings() {
        MenuItem itemImport = new MenuItem("Settings");
        itemImport.setOnAction(t -> {
            ResourceLoader resourceLoader = ResourceLoader.getInstance();
            FXMLLoader loader = AbstractController.getLoader(
                    resourceLoader.getClassLoader(),
                    resourceLoader.getRessource(ParamMapDialogController.class, "/map/editor/scene/paramMapDialog.fxml")
            );

            ParamMapDialogController controler = loader.getController();
            controler.loadMap(map);
            Stage stage = getStage(loader, String.format("Settings : %s", map.getTitle()), -1, -1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });
        mapParamButton.getItems().add(itemImport);
    }

    private Tab addTab(Layer layer) {
        int size = tabPane.getTabs().size();
        return addTab(size - 1, layer);
    }

    private Tab addTab(int index, Layer layer) {
        Tab tab = new Tab();
        tabPane.getTabs().add(index, tab);
        tab.setOnCloseRequest(event -> {
            map.remove(layer);
        });

        // Rename all tab and exclude add tab
        renameTab();

        return tab;
    }

    private void renameTab() {
        for (int i = 0; i < tabPane.getTabs().size() - 1; i++) {
            tabPane.getTabs().get(i).setText(String.format("Layer %d", i + 1));
        }
    }

    private void initTabPaneLayer() {

        Tab newTab = new Tab("+");
        newTab.setClosable(false);
        newTab.setId(ID_TAB_LAYER_ADD);
        tabPane.getTabs().add(newTab);

        if (map.getLayers().size() > 0) {
            for (int i = 0; i < map.getLayers().size(); i++) {
                Layer layer = map.getLayers().get(i);

                Tab tab = addTab(i, layer);
            }
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                this::onTabChanged
        );

        tabPane.getSelectionModel().select(0);


    }

    private void onTabChanged(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
        if (ID_TAB_LAYER_ADD.equals(t1.getId())) {
            Layer layer = MapHelper.addEmptylayer(map);
            Tab tab = addTab(layer);
            tab.setOnCloseRequest(event -> map.remove(layer));
            int size = tabPane.getTabs().size();
            if (size > 1) {
                tabPane.getSelectionModel().select(size - 2);
                selectLayerId = size - 2;
            }

        } else {
            selectLayerId = tabPane.getTabs().indexOf(t1);
        }
    }

    // TODO : rename
    private void initPaneMapEvent() {
        paneMap.setOnMouseEntered(e -> paneMap.requestFocus());


        paneMap.setOnKeyPressed(e -> {
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
            if (ctrlPressed) {
                scale += e.getDeltaY() > 0 ? 0.1 : -0.1;
                paneMap.getTransforms().clear();
                Scale scaleTransform = new Scale(scale, scale);
                scaleTransform.setPivotX(paneMap.getWidth() / 2);
                scaleTransform.setPivotY(paneMap.getHeight() / 2);
                paneMap.getTransforms().add(scaleTransform);
            }
        });
    }

    public void onMapResize(AbstractMessage message) {
        if (message instanceof MapResizeMessage) {
            boxImage.getChildren().clear();
            renderMap();
        }
    }

    public void onWindowsResize(AbstractMessage message) {
        if (message instanceof SceneResizeMessage) {
            SceneResizeMessage wMessage = (SceneResizeMessage) message;
            parent.setMaxWidth(wMessage.getWidth());
            parent.setMaxHeight(wMessage.getHeight());
        }
    }

    // TODO : rename
    private void initEvent() {
        EventSelectionAndDragged eventDrag = new EventSelectionAndDragged(paneMap, map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
        eventDrag.setOnRelease(area -> {
            if (tileSelected != null && selectLayerId >= 0) {
                Area maskArea = new Area(new Point2D(area.getBegin().getX(), area.getBegin().getY()),
                        new Point2D(area.getEnd().getX(), area.getEnd().getY()));

                AreaHelper.convertAreaToGrid(maskArea, map.getTileWidth(), map.getTileHeight());

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

                AreaHelper.convertAreaToGrid(maskArea, map.getTileWidth(), map.getTileHeight());

                AreaHelper.calculateAbsoluteArea(maskArea, tileSelected.getWidth(), tileSelected.getHeight());

                updateSelectionMask(maskArea);
            }
        });

        eventDrag.setOnMouve((area, event) -> {
            if (tileSelected != null) {
                double column = (int) Math.floor(event.getX() / map.getTileWidth());
                double line = (int) Math.floor(event.getY() / map.getTileHeight());

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
        this.map = new Map("test", 20, 20, 32, 32);

        MapHelper.addEmptylayer(map);

        selectLayerId = 0;

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

        rectangle.setWidth(area.getWidth() * map.getTileWidth() - strockeSized);
        rectangle.setY(area.getBegin().getY());
        rectangle.setHeight(area.getHeight() * map.getTileHeight() - strockeSized);
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
        cell.setWidth(map.getTileWidth() - 1);
        cell.setHeight(map.getTileHeight() - 1);
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
