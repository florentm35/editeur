package fr.florent.editor.core.message;

public abstract class AbstractMessage {
    private String key;

    protected AbstractMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
