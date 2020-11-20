package fr.florent;

import fr.florent.controller.AbstractController;
import fr.florent.controller.Controller;
import fr.florent.controller.editeur.TilePickerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        AbstractController.getStage(Controller.RESSOURCE_VIEW_PATH, "Ma fenêtre", (int) Screen.getPrimary().getBounds().getWidth() -200,
                (int) Screen.getPrimary().getBounds().getHeight() -200).show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
