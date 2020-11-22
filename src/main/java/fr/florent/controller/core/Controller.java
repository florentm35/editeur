package fr.florent.controller.core;

import fr.florent.controller.AbstractController;
import fr.florent.controller.editeur.mapeditor.MapEditorController;
import fr.florent.controller.editeur.tilepicker.TilePickerController;
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


            parent.add(mapEditorLoader.getRoot(), 1, 0);
            parent.add(tilePickerLoader.getRoot(), 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
