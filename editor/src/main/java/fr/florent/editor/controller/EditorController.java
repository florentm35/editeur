package fr.florent.editor.controller;

import fr.florent.editor.core.annotation.EnumScreenPosition;
import fr.florent.editor.core.annotation.ItemMenu;
import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.controller.AbstractController;
import fr.florent.editor.core.message.AbstractMessage;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SceneResizeMessage;
import fr.florent.editor.core.message.WindowsResizeMessage;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.editor.core.ressource.ResourceHelper;
import fr.florent.editor.ressource.EditorResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;


import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;


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
        parent.getStylesheets().add(EditorResourceLoader.getResource("/editor/css/editor.css").toString());


        ResourceLoader loader = ResourceLoader.getInstance();

        iniMenu();
        initScreen(loader);

        MessageSystem.getInstance().addObserver(WindowsResizeMessage.class.getName(), this::onWindowsResize);
    }

    // Refactor to add priority level
    private void iniMenu() {

        HashMap<String, Menu> cacheMenu = new HashMap<>();

        LOGGER.info("Begin load menu from EditorMenuFile.class");
        initMenuForClass(cacheMenu, EditorMenuFile.class);

        LOGGER.info("Begin load menu from module classloader");
        initMenuFromClassLoader(cacheMenu, ResourceLoader.getInstance().getClassLoader());

    }

    // TODO : refactor
    private void initMenuFromClassLoader(HashMap<String, Menu> cacheMenu, ClassLoader classLoader) {

        Set<Class<?>> menuClass = ResourceHelper.getTypesAnnotatedWith(classLoader, fr.florent.editor.core.annotation.Menu.class);
        for (Class tClass : menuClass) {
            initMenuForClass(cacheMenu, tClass);

        }
    }

    private void initMenuForClass(HashMap<String, Menu> cacheMenu, Class tClass) {
        fr.florent.editor.core.annotation.Menu menuAnnotation = (fr.florent.editor.core.annotation.Menu) tClass.getAnnotation(fr.florent.editor.core.annotation.Menu.class);
        String labelMenu = menuAnnotation.value();

        if (labelMenu != null && !labelMenu.isEmpty()) {
            if (!cacheMenu.containsKey(labelMenu)) {
                Menu mainMenu = new Menu(labelMenu);
                cacheMenu.put(labelMenu, mainMenu);
                menubar.getMenus().add(mainMenu);
            }


            Set<Method> methodes =
                    ResourceHelper.getAnnotedMethodFromClass(tClass, ItemMenu.class);

            for (Method method : methodes) {
                ItemMenu itemMenuAnnotation = method.getAnnotation(ItemMenu.class);
                MenuItem itemMenu = null;

                String itemLabel = itemMenuAnnotation.label();
                String parent = itemMenuAnnotation.parent();

                StringBuilder key = new StringBuilder(labelMenu);
                key.append("/");
                if (parent != null && !parent.isEmpty()) {
                    key.append(parent);
                    key.append("/");
                }

                itemMenu = new MenuItem(itemLabel);

                Menu parentMenu = getParentMenu(cacheMenu, key.toString());
                parentMenu.getItems().add(itemMenu);


                itemMenu.setOnAction(e -> {
                    try {
                        Object menuObject = tClass.getConstructor().newInstance();
                        method.invoke(menuObject);
                    } catch (Exception ex) {
                        LOGGER.info(ex.getMessage());
                        ex.printStackTrace();
                    }
                });

            }
        }
    }

    private Menu getParentMenu(HashMap<String, Menu> cacheMenu, String parent) {

        StringBuilder strSearch = new StringBuilder();

        Menu parentMenu = null;

        String[] paths = parent.split("/");
        for (String path : paths) {
            strSearch.append(path);

            if (!cacheMenu.containsKey(strSearch.toString())) {
                Menu tmp = new Menu(path);
                parentMenu.getItems().add(tmp);
                parentMenu = tmp;
            } else {
                parentMenu = cacheMenu.get(strSearch.toString());
            }
            strSearch.append("/");


        }
        return parentMenu;
    }

    private void initScreen(ResourceLoader loader) {
        for (Class tClass : loader.getTypesAnnotatedWith(Screen.class)) {
            Screen annotation = (Screen) tClass.getAnnotation(Screen.class);

            URL urlRessource = loader.getRessource(tClass, annotation.ressource());

            addScene(loader.getClassLoader(), urlRessource);
        }
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
