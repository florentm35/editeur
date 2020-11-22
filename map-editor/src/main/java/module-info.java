module map.editor {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires log4j;
    requires editor.core;
    requires map.core;
    requires tile.picker;

    exports fr.florent.map.editor.controller;
    exports fr.florent.map.editor.ressource;

}