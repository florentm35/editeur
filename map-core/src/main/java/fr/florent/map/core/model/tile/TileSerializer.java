package fr.florent.map.core.model.tile;

import com.google.gson.*;
import fr.florent.map.core.model.tileset.TileSet;
import fr.florent.map.core.model.tileset.TileSetHelper;

import java.lang.reflect.Type;

public class TileSerializer implements JsonSerializer<Tile>, JsonDeserializer<Tile> {

    @Override
    public Tile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String id = jsonElement.getAsJsonObject().get("id").getAsString();

        String data[] = id.split(";");

        TileSet tileSet = TileSetHelper.getTileSetFromId(data[0]);

        Tile tile = new Tile(tileSet, Double.parseDouble(data[1]), Double.parseDouble(data[2]));


        return tile;
    }

    @Override
    public JsonElement serialize(Tile tile, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject jsonTile = new JsonObject();
        jsonTile.addProperty("id", tile.getId());

        return jsonTile;
    }
}
