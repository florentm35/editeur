package fr.florent.map.core.model.layer;

import com.google.gson.*;
import fr.florent.editor.core.exception.RuntimeEditorException;
import fr.florent.editor.core.ressource.ResourceLoader;
import fr.florent.map.core.model.tile.Tile;
import fr.florent.map.core.model.tile.TileSerializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class LayerSerializer implements JsonSerializer<Layer>, JsonDeserializer<Layer> {


    @Override
    public Layer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Tile.class, new TileSerializer());

        Gson gson = gsonBuilder.create();

        String sClass = jsonElement.getAsJsonObject().get("class").getAsString();

        try {
            Class tClass = ResourceLoader.getInstance().getClassLoader().loadClass(sClass);

            Layer layer =  (Layer) gson.fromJson(jsonElement.toString(), tClass);

            layer.tiles = gson.fromJson(jsonElement.getAsJsonObject().get("tiles"), Tile[].class);

            return layer;
        } catch (ClassNotFoundException e) {
            throw new RuntimeEditorException(e);
        }

    }

    @Override
    public JsonElement serialize(Layer layer, Type type, JsonSerializationContext jsonSerializationContext) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Tile.class, new TileSerializer());

        Gson gson = gsonBuilder.create();

        JsonObject obj = new JsonObject();

        obj.add("tiles", gson.toJsonTree(layer.tiles));
        obj.addProperty("width", layer.width);
        obj.addProperty("height", layer.height);
        obj.addProperty("class", layer.getTypeClass().getName());

        return obj;
    }
}
