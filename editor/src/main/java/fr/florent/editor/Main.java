package fr.florent.editor;


import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.controller.Controller;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.WindowsResizeMessage;
import fr.florent.editor.core.properties.PropertiesUtil;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.editor.ressource.EditorRessourceLoader;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    @Override
    public void start(Stage primaryStage) throws Exception {

        loadModule();

        Stage stage = AbstractController.getStage(
                EditorRessourceLoader.getResource(Controller.RESSOURCE_VIEW_PATH),
                "Ma fenêtre", (int) Screen.getPrimary().getBounds().getWidth() - 200,
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

    public void loadModule(){
        PropertiesUtil.load(this.getClass().getClassLoader(), "application.properties");
        String pathModule = PropertiesUtil.getValue(PropertiesUtil.KEY_MODULE_PATH);
        ResourceLoader.getInstance().init(pathModule);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
