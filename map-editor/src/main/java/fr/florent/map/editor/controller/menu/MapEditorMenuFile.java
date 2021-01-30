package fr.florent.map.editor.controller.menu;

import fr.florent.editor.core.annotation.ItemMenu;
import fr.florent.editor.core.annotation.Menu;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.map.editor.controller.message.NewMapMessage;
import fr.florent.map.editor.controller.message.OpenMapMessage;

@Menu("File")
public class MapEditorMenuFile {

    @ItemMenu(label="map", parent = "open")
    public void openMap(){
        MessageSystem.getInstance().notify(new OpenMapMessage());
    }

    @ItemMenu(label = "map", parent = "new")
    public void newMap() {
        MessageSystem.getInstance().notify(new NewMapMessage());
    }

}
