package fr.florent.editor.core.ressource;

import fr.florent.editor.core.annotation.Screen;
import fr.florent.editor.core.exception.RuntimeEditorException;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class ResourceLoader {

    private static ResourceLoader instance = null;

    private URLClassLoader classLoader;

    private ResourceLoader() {
    }

    public static ResourceLoader getInstance(){
        if(instance == null){
            instance = new ResourceLoader();
        }
        return instance;
    }

    public void init(String path){
        classLoader = getModuleClassLoader(path);
    }

    public URL getRessource(Class tClass, String ressource){


        try {
            return classLoader.loadClass(tClass.getName()).getResource(ressource);
        } catch (ClassNotFoundException e) {
            throw new RuntimeEditorException(e);
        }
    }
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationCLass ){
        Reflections reflections = new Reflections(
                ConfigurationBuilder.build(classLoader)
        );
        return reflections.getTypesAnnotatedWith(annotationCLass);

    }

    private URLClassLoader getModuleClassLoader(String path) {

        if (path == null) {
            return null;
        }

        File moduleFolder = new File(path);

        if (moduleFolder.exists() && moduleFolder.isDirectory()) {

            File[] child = moduleFolder.listFiles();
            URL[] childURL = new URL[child.length];
            int i = 0;
            for (File file : child) {
                try {
                    childURL[i] = file.toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeEditorException(e);
                }
                i++;
            }


            URLClassLoader classLoader = new URLClassLoader(
                    childURL
            );

            return classLoader;
        }
        return null;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
