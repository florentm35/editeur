module map.core {

    requires javafx.graphics;
    requires editor.core;
    requires org.apache.commons.io;
    requires com.google.gson;


    exports fr.florent.map.core.event;
    exports fr.florent.map.core.helper;
    exports fr.florent.map.core.model.layer;
    exports fr.florent.map.core.model.selection;
    exports fr.florent.map.core.model.tile;
    exports fr.florent.map.core.model.tileset;
    exports fr.florent.map.core.model.map;
}