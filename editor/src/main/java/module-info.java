module editor {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires log4j;
    requires editor.core;

    // TODO:  GSon requiered from map-core delete this
    requires java.sql;

    exports fr.florent.editor.controller;

    opens fr.florent.editor ;
}