package fr.florent.map.editor.controller;

import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.map.core.model.map.Map;
import fr.florent.map.core.model.tileset.TileSet;
import fr.florent.map.core.model.tileset.TileSetFile;
import fr.florent.map.core.model.tileset.TileSetHelper;
import fr.florent.map.editor.controller.message.MapResizeMessage;
import fr.florent.tilepicker.message.TileSetCreateMessage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ParamMapDialogController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(ParamMapDialogController.class.getName());

    public VBox parent;

    public TextField nameField;
    public TextField tileWidthField;
    public TextField tileHeightField;
    public TextField numberColumnField;
    public TextField numberLineField;

    public Button saveButton;
    public Button cancelButton;


    private Map map;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parent.getStylesheets().add(
                ResourceLoader.getInstance().getRessource(this.getClass(), "/map/editor/css/mapEditor.css").toString());


        cancelButton.setOnAction(t -> {
            Stage stage = getStage(t);
            stage.close();
        });

        saveButton.setOnAction(t -> {
            Stage stage = getStage(t);
            save();
            stage.close();

        });

    }

    private void save() {
        map.setTitle(nameField.getText());

        try {
            int width = Integer.parseInt(numberColumnField.getText());
            int height = Integer.parseInt(numberLineField.getText());

            if (width != map.getWidth() || height != map.getHeight()) {
                map.resize(width, height);
            }

        } catch (NumberFormatException ex) {
            LOGGER.warn(ex.getMessage());
        }
        MessageSystem.getInstance().notify(new MapResizeMessage(map));

    }

    public void loadMap(Map map) {
        this.map = map;

        nameField.setText(map.getTitle());

        // TODO set to preference
        tileWidthField.setText(String.format("%d", map.getTileWidth()));
        tileHeightField.setText(String.format("%d", map.getTileHeight()));

        numberColumnField.setText(String.format("%d", map.getWidth()));
        numberLineField.setText(String.format("%d", map.getHeight()));

    }

    private Stage getStage(ActionEvent t) {
        Node source = (Node) t.getSource();
        return (Stage) source.getScene().getWindow();
    }


}
