module sample {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires log4j;

    exports fr.florent.controller;
    exports fr.florent.controller.core;
    exports fr.florent.controller.editeur.mapeditor;
    exports fr.florent.controller.editeur.tilepicker;

    opens fr.florent ;
}