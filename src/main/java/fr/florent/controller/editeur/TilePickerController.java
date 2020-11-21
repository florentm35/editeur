package fr.florent.controller.editeur;

import fr.florent.controller.AbstractController;
import fr.florent.controller.Controller;
import fr.florent.controller.editeur.event.IActionTileSelect;
import fr.florent.model.editeur.layer.TileLayer;
import fr.florent.model.editeur.tile.Tile;
import fr.florent.model.selection.Area;
import fr.florent.model.editeur.tileset.TileSet;
import fr.florent.composant.event.EventSelectionAndDragged;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TilePickerController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    public static final String RESSOURCE_VIEW_PATH = "/scene/tilePicker.fxml";
    public static final String RESSOURCE_TITLE = "Tile picker";

    public ImageView tilesetImage;
    public ScrollPane scrollTileSet;
    public Pane paneTileset;

    private IActionTileSelect onSelect;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image imagePng = new Image(getClass().getResourceAsStream("/tileset.png"));
        initTilePicker(new TileSet(imagePng, 32, 32));

        double height = Screen.getPrimary().getVisualBounds().getHeight();

        scrollTileSet.setMaxHeight(height - 20);
    }


    private void initTilePicker(TileSet tileSet) {

        scrollTileSet.setPrefViewportWidth(tileSet.getImagePng().getWidth() + 2);

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
     * On release selection of tile<br/>
     * Blit selection area
     *
     * @param tileSet
     * @param column
     * @param grid
     * @param rectangle
     * @param area
     */
    private void selectionOnRelease(TileSet tileSet, int column, List<Rectangle> grid, Rectangle rectangle, Area area) {
        paneTileset.getChildren().remove(rectangle);

        double x = area.getBeginAbsoluteX();
        double y = area.getBeginAbsoluteY();
        double width = area.getAbsoluteWidth();
        double height = area.getAbsoluteHeight();

        int xBegin = (int) Math.floor(x / tileSet.getTileWidth());
        int yBegin = (int) (Math.floor(y / tileSet.getTileHeight()));

        int xEnd = (int) Math.floor((x + width) / tileSet.getTileWidth()) + 1;
        int yEnd = (int) (Math.floor((y + height) / tileSet.getTileHeight())) + 1;

        clearSelection(grid);

        // TODO : Voir a séparer l'affichage de la création du TileLayer pour l'action
        TileLayer layer = new TileLayer(xEnd - xBegin, yEnd - yBegin);

        int xLayer = 0;
        for (int xGrid = xBegin; xGrid < xEnd; xGrid++) {
            int yLayer = 0;
            for (int yGrid = yBegin; yGrid < yEnd; yGrid++) {
                grid.get(xGrid + (yGrid * column)).setFill(Color.rgb(25, 25, 25, 0.5));
                layer.put(new Tile(tileSet, xGrid, yGrid), xLayer, yLayer);
                yLayer++;
            }
            xLayer++;
        }
        if (onSelect != null) {
            onSelect.action(layer);
        }
    }

    private void clearSelection(List<Rectangle> grid) {
        for (Rectangle cell : grid) {
            cell.setFill(Color.TRANSPARENT);
        }
    }

    /**
     * On dragged for tile selection<br/>
     * Change selection area
     *
     * @param rectangle
     * @param area
     */
    private void selectionOnDragged(Rectangle rectangle, Area area) {

        rectangle.setX(area.getBeginAbsoluteX());
        rectangle.setWidth(area.getAbsoluteWidth());
        rectangle.setY(area.getBeginAbsoluteY());
        rectangle.setHeight(area.getAbsoluteHeight());

        if (!paneTileset.getChildren().contains(rectangle)) {
            paneTileset.getChildren().add(rectangle);
        }

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
     * Event tile selection
     *
     * @param onSelect
     */
    public void setOnSelect(IActionTileSelect onSelect) {
        this.onSelect = onSelect;
    }


}
