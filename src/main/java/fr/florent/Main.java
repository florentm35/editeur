package fr.florent;

import fr.florent.controller.AbstractController;
import fr.florent.controller.core.Controller;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Stage stage = AbstractController.getStage(Controller.RESSOURCE_VIEW_PATH, "Ma fenêtre", (int) Screen.getPrimary().getBounds().getWidth() -200,
                (int) Screen.getPrimary().getBounds().getHeight() -200);

        stage.getHeight();
        stage.getWidth();
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
