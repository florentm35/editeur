package fr.florent;

import fr.florent.composant.message.MessageSystem;
import fr.florent.controller.AbstractController;
import fr.florent.controller.core.Controller;
import fr.florent.controller.core.message.WindowsResizeMessage;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    @Override
    public void start(Stage primaryStage) throws Exception {


        Stage stage = AbstractController.getStage(Controller.RESSOURCE_VIEW_PATH, "Ma fenêtre", (int) Screen.getPrimary().getBounds().getWidth() - 200,
                (int) Screen.getPrimary().getBounds().getHeight() - 200);

        stage.show();

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double height = stage.getHeight() - (Double.isNaN(stage.getY()) ? 0 : stage.getY());
            MessageSystem.getInstance().notify(new WindowsResizeMessage(width, height));
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double width = stage.getWidth();
            double height = newVal.doubleValue() - (Double.isNaN(stage.getY()) ? 0 : stage.getY());
            MessageSystem.getInstance().notify(new WindowsResizeMessage(width, height));
        });
        double width = stage.getWidth();
        double height = stage.getHeight() - (Double.isNaN(stage.getY()) ? 0 : stage.getY());

        MessageSystem.getInstance().notify(new WindowsResizeMessage(width, height));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
