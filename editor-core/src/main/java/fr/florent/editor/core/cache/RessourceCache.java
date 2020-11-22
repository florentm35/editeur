package fr.florent.editor.core.cache;

import java.util.WeakHashMap;

public class RessourceCache {

    private static WeakHashMap<String, IRessourceId> cache = new WeakHashMap<>();

    public static IRessourceId get(IRessourceId ressourceId){
        return get(ressourceId.getType(), ressourceId.getId());
    }
    public static IRessourceId get(String type, String id){
        return cache.get(type+"|"+id);
    }

    public static IRessourceId put(IRessourceId ressourceId){
        return cache.put(ressourceId.getType()+"|"+ressourceId.getId(), ressourceId);
    }
}
