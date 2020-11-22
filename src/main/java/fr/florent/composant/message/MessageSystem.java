package fr.florent.composant.message;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MessageSystem {
    public interface IActionMessage {
        void action(AbstractMessage message);
    }

    private HashMap<String, List<Pair<UUID, IActionMessage>>> cacheMessage;
    private static MessageSystem instance;

    private MessageSystem() {
        cacheMessage = new HashMap<>();
    }

    public static MessageSystem getInstance() {
        if (instance == null) {
            instance = new MessageSystem();
        }
        return instance;
    }

    public void notify(AbstractMessage message) {
        synchronized (cacheMessage) {
            if (cacheMessage.containsKey(message.getKey())) {
                for (Pair<UUID, IActionMessage> action : cacheMessage.get(message.getKey())) {
                    if (action.getValue() != null) {
                        action.getValue().action(message);
                    }
                }
            }
        }
    }

    public UUID addObserver(String key, IActionMessage listener) {
        synchronized (cacheMessage) {
            List<Pair<UUID, IActionMessage>> tmp;
            if (!cacheMessage.containsKey(key)) {
                tmp = new ArrayList<>();
                cacheMessage.put(key, tmp);
            } else {
                tmp = cacheMessage.get(key);
            }

            UUID id = UUID.randomUUID();
            Pair<UUID, IActionMessage> pair = new Pair<>(id, listener);
            tmp.add(pair);

            return id;
        }
    }

    public void deleteObserver(String key, UUID id) {
        synchronized (cacheMessage) {
            if (cacheMessage.containsKey(key)) {
                Pair<UUID, IActionMessage> actionToDelete = null;
                List<Pair<UUID, IActionMessage>> tmp = cacheMessage.get(key);
                for (Pair<UUID, IActionMessage> action : tmp) {
                    if (action.getKey() != null && action.getKey().equals(id)) {
                        actionToDelete = action;
                        break;
                    }
                }
                if (actionToDelete != null) {
                    tmp.remove(actionToDelete);
                }
            }
        }
    }

}
