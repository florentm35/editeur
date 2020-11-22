package fr.florent.editor.ressource;

import java.net.URL;

public class EditorRessourceLoader {
    public static URL getResource(String ressource){
        return EditorRessourceLoader.class.getResource(ressource);
    }
}
