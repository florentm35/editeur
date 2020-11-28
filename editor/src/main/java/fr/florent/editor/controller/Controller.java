package fr.florent.editor.controller;

import fr.florent.editor.core.annotation.EnumScreenPosition;
import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SceneResizeMessage;
import fr.florent.editor.core.message.WindowsResizeMessage;
import fr.florent.editor.core.ressource.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());


    public static final String RESSOURCE_VIEW_PATH = "/editor/scene/sample.fxml";


    public double leftPaneWidth = 0;
    public double rightPaneWidth = 0;
    public Pane leftPane;
    public Pane centerPane;
    public Pane rightPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ResourceLoader loader = ResourceLoader.getInstance();

        for (Class tClass :  loader.getTypesAnnotatedWith(Screen.class)) {
            Screen annotation = (Screen) tClass.getAnnotation(Screen.class);

            URL urlRessource = loader.getRessource(tClass,annotation.ressource());

            addScene(loader.getClassLoader(), urlRessource);

        }

        MessageSystem.getInstance().addObserver(WindowsResizeMessage.class.getName(), this::onWindowsResize);
    }

    public void onWindowsResize(AbstractMessage message) {
        if (message instanceof WindowsResizeMessage) {
            WindowsResizeMessage wMessage = (WindowsResizeMessage) message;

            leftPane.setMinWidth(leftPaneWidth);
            rightPane.setMinWidth(rightPaneWidth);
            centerPane.setMaxWidth(wMessage.getWidth() - leftPaneWidth - rightPaneWidth);
            centerPane.setPrefWidth(wMessage.getWidth() - leftPaneWidth - rightPaneWidth);

            MessageSystem.getInstance().notify(new SceneResizeMessage(EnumScreenPosition.LEFT, leftPaneWidth, wMessage.getHeight()));
            MessageSystem.getInstance().notify(new SceneResizeMessage(EnumScreenPosition.CENTER, wMessage.getWidth() - leftPaneWidth - rightPaneWidth, wMessage.getHeight()));
        }
    }

    public void addScene(ClassLoader classLoader, URL ressource) {
        try {
            FXMLLoader loader = AbstractController.getLoader(classLoader, ressource);
            AbstractController controller = loader.getController();
            Pane node = loader.getRoot();

            Screen screenAnnotation = controller.getClass().getAnnotation(Screen.class);

            if (screenAnnotation != null) {
                switch (screenAnnotation.value()) {
                    case LEFT:
                        leftPane.getChildren().add(node);
                        node.widthProperty().addListener((obs, oldVal, newVal) -> {
                            leftPaneWidth = newVal.doubleValue();
                        });
                        break;
                    case CENTER:
                        centerPane.getChildren().add(node);
                        break;
                    case RIGHT:
                        rightPane.getChildren().add(node);
                        node.widthProperty().addListener((obs, oldVal, newVal) -> {
                            rightPaneWidth = newVal.doubleValue();
                        });
                        break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger TilePickerController.RESSOURCE_VIEW_PATH", e);
        }
    }


}
