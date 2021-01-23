package fr.florent.map.core.model.map;

import fr.florent.map.core.model.layer.Layer;
import fr.florent.map.core.model.layer.TileLayer;

public class MapHelper {

    /**
     * Add empty Layer
     * @param map
     * @return The created Layer
     */
    public static Layer addEmptylayer(Map map){
        Layer layer = new TileLayer(map.getWidth(), map.getHeight());
        map.addlayer(layer);
        return layer;
    }
}
