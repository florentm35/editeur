module editor.core {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires reflections;

    exports fr.florent.editor.core.annotation;
    exports fr.florent.editor.core.cache;
    exports fr.florent.editor.core.event;
    exports fr.florent.editor.core.message;
    exports fr.florent.editor.core.controller;
    exports fr.florent.editor.core.event.util;
    exports fr.florent.editor.core.properties;
    exports fr.florent.editor.core.ressource;


}