module editor.core {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;

    exports fr.florent.editor.core.annotation;
    exports fr.florent.editor.core.cache;
    exports fr.florent.editor.core.event;
    exports fr.florent.editor.core.message;
    exports fr.florent.editor.core.controller;
    exports fr.florent.editor.core.event.util;
}