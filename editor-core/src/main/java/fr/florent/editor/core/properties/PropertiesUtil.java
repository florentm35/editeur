package fr.florent.editor.core.properties;

import fr.florent.editor.core.exception.RuntimeEditorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static final String KEY_PATH = "editor.path";

    public static final String KEY_MODULE_PATH = "editor.module.path";

    public static final String KEY_TILESET_PATH = "editor.resource.tileset.path";

    public static final String KEY_LIB_PATH = "editor.lib.path";
    public static Properties prop = null;

    public static void load(ClassLoader loader, String ressource) {
        prop = new Properties();

        try (InputStream input = loader.getResourceAsStream(ressource)) {
            prop.load(input);
        } catch (final IOException io) {
            throw new RuntimeEditorException(io);
        }

    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }

}
