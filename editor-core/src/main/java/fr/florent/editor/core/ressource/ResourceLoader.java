package fr.florent.editor.core.ressource;

import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.exception.RuntimeEditorException;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Set;


public class ResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class.getName());

    private static ResourceLoader instance = null;

    private URLClassLoader classLoader;

    private ResourceLoader() {
    }

    public static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    public void init(String... path) {
        classLoader = getModuleClassLoader(path);
    }

    public URL getRessource(Class tClass, String ressource) {

        try {
            return classLoader.loadClass(tClass.getName()).getResource(ressource);
        } catch (ClassNotFoundException e) {
            throw new RuntimeEditorException(e);
        }
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationCLass) {
        Reflections reflections = new Reflections(
                ConfigurationBuilder.build(classLoader)
        );
        return reflections.getTypesAnnotatedWith(annotationCLass);

    }

    private URLClassLoader getModuleClassLoader(String... path) {

        if (path == null) {
            return null;
        }

        ArrayList<URL> listJar = new ArrayList<>();

        for (String subPath : path) {
            LOGGER.info("Chargement des jar :" + subPath);
            File moduleFolder = new File(subPath);
            for (File file : moduleFolder.listFiles()) {
                try {
                    listJar.add(file.toURI().toURL());
                    LOGGER.info(file.getName());
                } catch (MalformedURLException e) {
                    throw new RuntimeEditorException(e);
                }
            }
        }
        URL[] tmp = new URL[listJar.size()];
        URLClassLoader classLoader = new URLClassLoader(
                listJar.toArray(tmp)
        );
        return classLoader;

    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
