package fr.florent.editor.ressource;

import java.net.URL;

public class EditorResourceLoader {
    public static URL getResource(String ressource){
        return EditorResourceLoader.class.getResource(ressource);
    }
}
