package fr.florent.editor.core.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractController implements Initializable {

    //TODO: Voir comment refaire ce truc
    public static Stage getStage(URL ressource, String title) throws IOException {
        return getStage(ressource, title, -1, -1);
    }

    public static FXMLLoader getLoader(URL url) throws IOException {
        return getLoader(null, url);
    }

    public static FXMLLoader getLoader(ClassLoader classLoader, URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        if (classLoader != null) {
            loader.setClassLoader(classLoader);
        }
        loader.load();
        return loader;
    }

    public static Stage getStage(URL ressource, String title, int width, int height) throws IOException {


        FXMLLoader root = getLoader(ressource);
        Scene secondScene = new Scene(root.getRoot(), width, height);
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setScene(secondScene);
        return newWindow;
    }


}
