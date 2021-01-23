package fr.florent.tilepicker;


import fr.florent.editor.core.annotation.EnumScreenPosition;
import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SceneResizeMessage;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.editor.core.util.Item;
import fr.florent.map.core.helper.AreaHelper;
import fr.florent.map.core.helper.EventSelectionAndDragged;
import fr.florent.map.core.model.layer.TileLayer;
import fr.florent.map.core.model.selection.Area;
import fr.florent.map.core.model.tile.Tile;
import fr.florent.map.core.model.tileset.TileSet;
import fr.florent.map.core.model.tileset.TileSetHelper;
import fr.florent.tilepicker.message.TileSelectedMessage;
import fr.florent.tilepicker.message.TileSetCreateMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Screen(value = EnumScreenPosition.LEFT, ressource = TilePickerController.RESSOURCE_VIEW_PATH)
public class TilePickerController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(TilePickerController.class.getName());
    public static final String RESSOURCE_VIEW_PATH = "/tile-picker/scene/tilePicker.fxml";
    public static final String RESSOURCE_TITLE = "Tile picker";
    public VBox parent;

    // Menu
    public ComboBox<Item<TileSet>> tileSetComboBox;
    public MenuButton tileSelectOption;

    // Tile selector
    public ScrollPane scrollTileSet;
    public Pane paneTileset;
    public ImageView tilesetImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        parent.getStylesheets().add(ResourceLoader.getInstance().getRessource(this.getClass(), "/tile-picker/css/tilePicker.css").toString());


        initMenu();
        MessageSystem.getInstance().addObserver(SceneResizeMessage.getKey(EnumScreenPosition.LEFT), this::onWindowsResize);

    }

    private void initMenu() {
        initTileSetComboBox();

        MenuItem itemImport = new MenuItem("Import");
        itemImport.setOnAction(t -> {
            ResourceLoader resourceLoader = ResourceLoader.getInstance();
            FXMLLoader loader = AbstractController.getLoader(
                    resourceLoader.getClassLoader(),
                    resourceLoader.getRessource(TilePickerController.class, "/tile-picker/scene/importDialog.fxml")
            );
            Stage stage = getStage(loader, "test", -1, -1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });
        tileSelectOption.getItems().add(itemImport);
    }

    private void initTileSetComboBox() {
        List<TileSet> lstTileSet = TileSetHelper.getListTileSet();

        for (TileSet tileSet : lstTileSet) {
            tileSetComboBox.getItems().add(new Item<>(tileSet, tileSet.getName()));
        }

        tileSetComboBox.getSelectionModel().selectedIndexProperty().addListener((ov, oldvalue, newvalue) -> {
            refreshTilePicker(tileSetComboBox.getValue().getValue());
        });
        tileSetComboBox.setValue(new Item(lstTileSet.get(0), lstTileSet.get(0).getName()));

        MessageSystem.getInstance().addObserver(TileSetCreateMessage.class.getName(), message -> {
            if (message instanceof TileSetCreateMessage) {
                TileSet tileSet = ((TileSetCreateMessage) message).getTileSet();
                tileSetComboBox.getItems().add(
                        new Item<>(tileSet, tileSet.getName())
                );
            }
        });
    }


    private void refreshTilePicker(TileSet tileSet) {

        scrollTileSet.setPrefViewportWidth(tileSet.getImagePng().getWidth() + 10);

        tilesetImage.setImage(tileSet.getImagePng());
        tilesetImage.setViewOrder(2);

        double column = Math.floor(tileSet.getImagePng().getWidth() / tileSet.getTileWidth());
        double line = Math.floor(tileSet.getImagePng().getHeight() / tileSet.getTileHeight());

        List<Rectangle> grid = new ArrayList<>();

        initTilePickerGrid(tileSet, column, line, grid);


        Rectangle rectangle = new Rectangle();
        rectangle.setViewOrder(0);
        rectangle.setFill(Color.rgb(0, 0, 1, 0.5));
        rectangle.setStroke(Color.BLUE);

        EventSelectionAndDragged selection = new EventSelectionAndDragged(paneTileset, (int) tileSet.getImagePng().getWidth(),
                (int) tileSet.getImagePng().getHeight());

        selection.setOnDragged(area -> {
            selectionOnDragged(rectangle, area);
        });

        selection.setOnRelease(area -> {
            selectionOnRelease(tileSet, (int) column, grid, rectangle, area);
        });
    }

    /**
     * Create the grid of tileset for the picker
     *
     * @param tileSet
     * @param column
     * @param line
     * @param grid
     */
    private void initTilePickerGrid(TileSet tileSet, double column, double line, List<Rectangle> grid) {

        for (double i = 0; i < (column * line); i++) {
            Rectangle cell = new Rectangle();
            cell.setStroke(Color.LIGHTGRAY);
            cell.setFill(Color.TRANSPARENT);
            cell.setWidth(tileSet.getTileWidth());
            cell.setHeight(tileSet.getTileHeight());
            cell.setStrokeWidth(2);

            double x = (i % column) * tileSet.getTileWidth();
            double y = Math.floor(i / column) * tileSet.getTileHeight();
            cell.setX(x);
            cell.setY(y);
            cell.setViewOrder(1);
            paneTileset.getChildren().add(cell);
            grid.add(cell);

        }
    }

    /**
     * On release fr.florent.map.core.selection of tile<br/>
     * Blit fr.florent.map.core.selection area
     *
     * @param tileSet
     * @param column
     * @param grid
     * @param rectangle
     * @param area
     */
    private void selectionOnRelease(TileSet tileSet, int column, List<Rectangle> grid, Rectangle rectangle, Area area) {
        paneTileset.getChildren().remove(rectangle);


        AreaHelper.convertAreaToGrid(area, tileSet.getTileWidth(), tileSet.getTileHeight());

        AreaHelper.calculateAbsoluteArea(area);

        clearSelection(grid);

        // TODO : Refactor to external wiew change and TileLayer creation
        TileLayer layer = new TileLayer(area.getWidth(), area.getHeight());

        int xLayer = 0;
        for (double xGrid = area.getBegin().getX(); xGrid <= area.getEnd().getX(); xGrid++) {
            int yLayer = 0;
            for (double yGrid = area.getBegin().getY(); yGrid <= area.getEnd().getY(); yGrid++) {
                grid.get(
                        (int) (xGrid + (yGrid * column))
                ).setFill(Color.rgb(25, 25, 25, 0.5));
                layer.put(new Tile(tileSet, xGrid, yGrid), xLayer, yLayer);
                yLayer++;
            }
            xLayer++;
        }

        TileSelectedMessage message = new TileSelectedMessage(layer);

        MessageSystem.getInstance().notify(message);
    }

    private void clearSelection(List<Rectangle> grid) {
        for (Rectangle cell : grid) {
            cell.setFill(Color.TRANSPARENT);
        }
    }

    public void onWindowsResize(AbstractMessage message) {
        if (message instanceof SceneResizeMessage) {
            SceneResizeMessage wMessage = (SceneResizeMessage) message;
            parent.setMaxHeight(wMessage.getHeight());
            parent.setPrefHeight(wMessage.getHeight());
        }
    }

    /**
     * On dragged for tile fr.florent.map.core.selection<br/>
     * Change fr.florent.map.core.selection area
     *
     * @param rectangle
     * @param area
     */
    private void selectionOnDragged(Rectangle rectangle, Area area) {

        rectangle.setX(Math.min(
                area.getBegin().getX(),
                area.getEnd().getX()
        ));
        rectangle.setY(Math.min(
                area.getBegin().getY(),
                area.getEnd().getY()
        ));
        rectangle.setWidth(Math.abs(area.getWidth()));
        rectangle.setHeight(Math.abs(area.getHeight()));

        if (!paneTileset.getChildren().contains(rectangle)) {
            paneTileset.getChildren().add(rectangle);
        }

    }
}
