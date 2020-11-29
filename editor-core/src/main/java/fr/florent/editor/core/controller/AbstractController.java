package fr.florent.editor.core.controller;

import fr.florent.editor.core.exception.RuntimeEditorException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

//TODO: Refactor
public abstract class AbstractController implements Initializable {


    public static FXMLLoader getLoader(URL url) {
        return getLoader(null, url);
    }

    public static FXMLLoader getLoader(ClassLoader classLoader, URL url) {
        FXMLLoader loader = new FXMLLoader(url);
        if (classLoader != null) {
            loader.setClassLoader(classLoader);
        }
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }
        return loader;
    }

    public static Stage getStage(FXMLLoader root, String title, int width, int height) {
        Scene secondScene = new Scene(root.getRoot(), width, height);
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setScene(secondScene);
        return newWindow;
    }

    public static Stage getStage(URL ressource, String title, int width, int height) {
        FXMLLoader root = getLoader(ressource);

        return getStage(root, title, width, height);
    }

    public static Stage getStage(URL ressource, String title) throws IOException {
        return getStage(ressource, title, -1, -1);
    }

}
