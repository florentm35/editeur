module editor {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires log4j;
    requires editor.core;

    // Before OSGI
    requires tile.picker;
    requires map.editor;
    requires map.core;

    exports fr.florent.editor.controller;


    opens fr.florent.editor ;
}