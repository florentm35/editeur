package fr.florent.map.editor.ressource;

import java.net.URL;

public class MapEditorRessourceLoader {
    public static URL getResource(String ressource){
        return MapEditorRessourceLoader.class.getResource(ressource);
    }
}
