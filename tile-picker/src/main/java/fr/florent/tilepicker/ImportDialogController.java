package fr.florent.tilepicker;

import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.map.core.model.tileset.TileSet;
import fr.florent.map.core.model.tileset.TileSetFile;
import fr.florent.map.core.model.tileset.TileSetHelper;
import fr.florent.tilepicker.message.TileSelectedMessage;
import fr.florent.tilepicker.message.TileSetCreateMessage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ImportDialogController extends AbstractController {
    public VBox parent;

    public TextField nameField;
    public TextField tileWidthField;
    public TextField tileHeightField;

    public TextField imageFileField;
    public Button selectImageFile;

    public Button saveButton;
    public Button cancelButton;

    private File fileSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parent.getStylesheets().add(
                ResourceLoader.getInstance().getRessource(this.getClass(), "/tile-picker/css/tilePicker.css").toString());

        // TODO set to preference
        tileWidthField.setText("32");
        tileHeightField.setText("32");

        final FileChooser fileChooser = new FileChooser();
        selectImageFile.setOnAction(t -> {
            Stage stage = getStage(t);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                fileSelected = file;
                imageFileField.setText(file.getName());
            }
        });

        cancelButton.setOnAction(t -> {
            Stage stage = getStage(t);
            stage.close();
        });

        saveButton.setOnAction(t -> {
            Stage stage = getStage(t);

            TileSet tileSet = new TileSet();
            tileSet.setName(nameField.getText());
            tileSet.setTileWidth(Integer.parseInt(tileWidthField.getText()));
            tileSet.setTileHeight(Integer.parseInt(tileHeightField.getText()));
            tileSet.setImagePng(TileSetHelper.loadTileSetImage(fileSelected));

            TileSetFile tileSetFile = TileSetHelper.getFileFromTileSet(tileSet);
            TileSetHelper.saveTileSet(tileSetFile, fileSelected);

            MessageSystem.getInstance().notify(new TileSetCreateMessage(tileSet));

            stage.close();
        });

    }

    private Stage getStage(ActionEvent t) {
        Node source = (Node) t.getSource();
        return (Stage) source.getScene().getWindow();
    }


}
