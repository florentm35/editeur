package fr.florent.editor.core.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static final String KEY_MODULE_PATH = "editor.module.path";

    public static Properties prop = null;

    public static void load(ClassLoader loader, String ressource) {
        prop = new Properties();
        InputStream input = null;

        try {

            input = loader.getResourceAsStream(ressource);

            prop.load(input);

        } catch (final IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String getValue(String key){
        return prop.getProperty(key);
    }

}
