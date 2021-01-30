package fr.florent.editor.controller;

import fr.florent.editor.core.annotation.ItemMenu;
import fr.florent.editor.core.annotation.Menu;
import fr.florent.editor.core.message.MessageSystem;
import fr.florent.editor.core.message.SaveMessage;
import javafx.application.Platform;
import org.apache.log4j.Logger;

@Menu(value = "File", priority = 0)
public class EditorMenuFile {
    private static final Logger LOGGER = Logger.getLogger(EditorMenuFile.class.getName());

    @ItemMenu(label = "save", priority = 2)
    public void saveItem(){
        MessageSystem.getInstance().notify(new SaveMessage());
    }

    @ItemMenu(label = "exit", priority = ItemMenu.MAX_LEVEL)
    public void exitItem(){

        Platform.exit();
        System.exit(0);
    }
}
