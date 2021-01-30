package fr.florent.map.core.model.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.florent.editor.core.exception.RuntimeEditorException;
import fr.florent.editor.core.properties.PropertiesUtil;
import fr.florent.map.core.model.layer.Layer;
import fr.florent.map.core.model.layer.LayerSerializer;
import fr.florent.map.core.model.layer.TileLayer;
import fr.florent.map.core.model.tile.Tile;
import fr.florent.map.core.model.tile.TileSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MapHelper {

    /**
     * Add empty Layer
     *
     * @param map
     * @return The created Layer
     */
    public static Layer addEmptylayer(Map map) {
        Layer layer = new TileLayer(map.getWidth(), map.getHeight());
        map.addlayer(layer);
        return layer;
    }

    //Rewrite without Gson
    public static void saveMap(Map map) {
        StringBuilder url = getMapPath();

        url.append(map.getId()).append(".json");
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Tile.class, new TileSerializer());
        gsonBuilder.registerTypeAdapter(TileLayer.class, new LayerSerializer());

        Gson gson = gsonBuilder.create();

        String data = gson.toJson(map);

        File file = new File(url.toString());
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }

    }

    // Rewrite without Gson and look for perf
    public static Map loadMap(File mapFile) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Tile.class, new TileSerializer());
        gsonBuilder.registerTypeAdapter(Layer.class, new LayerSerializer());

        Gson gson = gsonBuilder.create();

        try {
            String content = Files.readString(mapFile.toPath());

            return gson.fromJson(content, Map.class);

        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }
    }

    public static StringBuilder getMapPath() {
        String path = PropertiesUtil.getValue(PropertiesUtil.KEY_PATH);
        String maps = PropertiesUtil.getValue(PropertiesUtil.KEY_MAP_PATH);
        StringBuilder url = new StringBuilder();
        url.append(path).append(File.separator).append(maps).append(File.separator);
        return url;
    }
}
