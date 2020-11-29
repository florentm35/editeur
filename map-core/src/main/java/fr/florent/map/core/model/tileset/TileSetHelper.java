package fr.florent.map.core.model.tileset;

import fr.florent.editor.core.exception.RuntimeEditorException;
import fr.florent.editor.core.properties.PropertiesUtil;
import fr.florent.editor.core.util.Pair;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.commons.io.FilenameUtils;

// TODO : refactor
public class TileSetHelper {

    public static TileSetFile getFileFromTileSet(TileSet tileSet) {

        TileSetFile file = new TileSetFile();
        file.setId(tileSet.getId());
        file.setName(tileSet.getName());
        file.setTileHeight(tileSet.getTileHeight());
        file.setTileWidth(tileSet.getTileWidth());

        return file;
    }

    public static TileSet getTileSetFromFile(TileSetFile file, File imageFile) {

        TileSet tileSet = new TileSet(file.getId());
        tileSet.setName(file.getName());
        tileSet.setTileHeight(file.getTileHeight());
        tileSet.setTileWidth(file.getTileWidth());
        tileSet.setImagePng(loadTileSetImage(imageFile));

        return tileSet;
    }

    public static Image loadTileSetImage(File imageFile) {
        try (FileInputStream is = new FileInputStream(imageFile)) {
            return new Image(is);
        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }
    }

    public static void saveTileSet(TileSetFile tileSetFile, File imageFile) {

        String path = PropertiesUtil.getValue(PropertiesUtil.KEY_PATH);
        String ressource = PropertiesUtil.getValue(PropertiesUtil.KEY_TILESET_PATH);
        StringBuilder url = new StringBuilder();
        url.append(path).append(File.separator).append(ressource).append(File.separator);

        String extenstion = getExtension(imageFile.getAbsolutePath());

        // Copie
        Path destination = Paths.get(url.toString() + File.separator + tileSetFile.getId() + "." + extenstion);
        try {
            Files.copy(imageFile.toPath(), destination);
        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }


        String data = format(tileSetFile);

        File file = new File(url.toString() + File.separator + tileSetFile.getId() + ".json");
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeEditorException(e);
        }

    }

    // TODO : refactor
    public static List<TileSet> getListTileSet() {
        String path = PropertiesUtil.getValue(PropertiesUtil.KEY_PATH);
        String resourcePath = PropertiesUtil.getValue(PropertiesUtil.KEY_TILESET_PATH);
        StringBuilder url = new StringBuilder();
        url.append(path).append(File.separator).append(resourcePath).append(File.separator);

        Map<String, Pair<TileSetFile, File>> mapTileSet = new HashMap<>();
        File resourceFile = new File(url.toString());
        for (File resource : resourceFile.listFiles()) {

            String ext = getExtension(resource.getName());
            String id = resource.getName().replace(ext, "");
            Pair<TileSetFile, File> tileSetinfo = null;
            if (!mapTileSet.containsKey(id)) {
                tileSetinfo = new Pair<>();
                mapTileSet.put(id, tileSetinfo);
            } else {
                tileSetinfo = mapTileSet.get(id);
            }
            if ("json".equals(ext)) {
                try (FileInputStream is = new FileInputStream(resource)) {
                    String data = new String(is.readAllBytes());
                    tileSetinfo.setKey(converse(data, TileSetFile.class));
                } catch (IOException e) {
                    throw new RuntimeEditorException(e);
                }
            } else {
                tileSetinfo.setValue(resource);
            }
        }
        List<TileSet> result = new ArrayList<>();
        for (Map.Entry<String, Pair<TileSetFile, File>> entry : mapTileSet.entrySet()) {
            result.add(getTileSetFromFile(entry.getValue().getKey(), entry.getValue().getValue()));
        }
        return result;
    }

    // TODO : rewrite without Gson
    public static String format(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T converse(String data, Class<T> tclass) {
        Gson gson = new Gson();
        return gson.fromJson(data, tclass);
    }

    // TODO : rewrite without common-io
    public static String getExtension(String file) {
        return FilenameUtils.getExtension(file);
    }

}
