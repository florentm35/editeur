package fr.florent.tilepicker.ressource;

import java.net.URL;

public class TilePickerRessourceLoader {
    public static URL getResource(String ressource){
        return TilePickerRessourceLoader.class.getResource(ressource);
    }
}
