package fr.florent.editor.controller;

import fr.florent.editor.core.annotation.EnumScreenPosition;
import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SceneResizeMessage;
import fr.florent.editor.core.message.WindowsResizeMessage;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.editor.ressource.EditorRessourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;


public class EditorController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(EditorController.class.getName());


    public static final String RESSOURCE_VIEW_PATH = "/editor/scene/editor.fxml";

    public VBox parent;

    public MenuBar menubar;

    public Pane leftPane;
    public Pane centerPane;
    public Pane rightPane;

    public double leftPaneWidth = 0;
    public double rightPaneWidth = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parent.getStylesheets().add(EditorRessourceLoader.getResource("/editor/css/editor.css").toString());


        ResourceLoader loader = ResourceLoader.getInstance();

        for (Class tClass : loader.getTypesAnnotatedWith(Screen.class)) {
            Screen annotation = (Screen) tClass.getAnnotation(Screen.class);

            URL urlRessource = loader.getRessource(tClass, annotation.ressource());

            addScene(loader.getClassLoader(), urlRessource);
        }

        Menu fileMenu = new Menu("File");
        Menu newMenu = new Menu("new");
        fileMenu.getItems().add(newMenu);


        MenuItem newMapMenu = new MenuItem("Map");
        newMenu.getItems().add(newMapMenu);

        menubar.getMenus().add(fileMenu);
        Menu viewMenu = new Menu("View");
        menubar.getMenus().add(viewMenu);


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


    }


}
