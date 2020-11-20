package fr.florent.controller;

import fr.florent.controller.editeur.MapEditorController;
import fr.florent.controller.editeur.TilePickerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());


    public static final String RESSOURCE_VIEW_PATH = "/scene/sample.fxml";
    public GridPane parent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            FXMLLoader mapEditorLoader = getLoader(MapEditorController.RESSOURCE_VIEW_PATH);
            FXMLLoader tilePickerLoader = getLoader(TilePickerController.RESSOURCE_VIEW_PATH);

            // TODO : Changer le fonctionnement par un pattern Subscriber
            MapEditorController mapEditorController = mapEditorLoader.getController();
            TilePickerController tilePickerController = tilePickerLoader.getController();
            tilePickerController.setOnSelect(mapEditorController::onChangeTileSelection);

            parent.add(mapEditorLoader.getRoot(), 1, 0);
            parent.add(tilePickerLoader.getRoot(), 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
