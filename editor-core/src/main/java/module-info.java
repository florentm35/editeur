module editor.core {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires reflections8;
    requires log4j;

    exports fr.florent.editor.core.annotation;
    exports fr.florent.editor.core.cache;
    exports fr.florent.editor.core.event;
    exports fr.florent.editor.core.message;
    exports fr.florent.editor.core.controller;
    exports fr.florent.editor.core.event.util;
    exports fr.florent.editor.core.properties;
    exports fr.florent.editor.core.ressource;
    exports fr.florent.editor.core.exception;
    exports fr.florent.editor.core.util;

}