package fr.florent.editor.core.cache;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractRessourceAutoId {

    protected String id;

    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    protected void setId(String id){
        this.id = id;
    }

    public String getType() {
        return this.getClass().getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractRessourceAutoId that = (AbstractRessourceAutoId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
