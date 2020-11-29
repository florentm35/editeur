package fr.florent.editor.core.util;

import java.util.Objects;

public class Item<T> {
    private T value;
    private String label;

    public Item(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item<?> item = (Item<?>) o;
        return Objects.equals(label, item.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
