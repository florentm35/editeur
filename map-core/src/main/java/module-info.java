module map.core {

    requires javafx.graphics;
    requires editor.core;

    exports fr.florent.map.core.event;
    exports fr.florent.map.core.helper;
    exports fr.florent.map.core.model;
    exports fr.florent.map.core.model.layer;
    exports fr.florent.map.core.model.selection;
    exports fr.florent.map.core.model.tile;
    exports fr.florent.map.core.model.tileset;
}