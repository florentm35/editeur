package fr.florent.controller.core;

import fr.florent.composant.message.AbstractMessage;
import fr.florent.composant.message.MessageSystem;
import fr.florent.controller.AbstractController;
import fr.florent.controller.core.annotation.EnumScreenPosition;
import fr.florent.controller.core.annotation.Screen;
import fr.florent.controller.core.message.SceneResizeMessage;
import fr.florent.controller.core.message.WindowsResizeMessage;
import fr.florent.controller.editeur.mapeditor.MapEditorController;
import fr.florent.controller.editeur.tilepicker.TilePickerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());


    public static final String RESSOURCE_VIEW_PATH = "/scene/sample.fxml";


    public double leftPaneWidth = 0;
    public double rightPaneWidth = 0;
    public Pane leftPane;
    public Pane centerPane;
    public Pane rightPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addScene(TilePickerController.RESSOURCE_VIEW_PATH);
        addScene(MapEditorController.RESSOURCE_VIEW_PATH);

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

    public void addScene(String ressource) {
        try {
            FXMLLoader loader = getLoader(ressource);
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
