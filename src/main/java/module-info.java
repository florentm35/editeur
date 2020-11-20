module sample {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires log4j;

    exports fr.florent.controller;
    exports fr.florent.controller.editeur;

    opens fr.florent ;
}