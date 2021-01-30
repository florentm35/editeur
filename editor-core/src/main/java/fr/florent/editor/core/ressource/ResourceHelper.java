package fr.florent.editor.core.ressource;

import org.reflections8.Reflections;
import org.reflections8.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class ResourceHelper {


    public static Set<Class<?>> getTypesAnnotatedWith(ClassLoader classLoader, Class<? extends Annotation> annotationCLass) {
        Reflections reflections = new Reflections(
                ConfigurationBuilder.build(classLoader)
        );
        return reflections.getTypesAnnotatedWith(annotationCLass);

    }


    /**
     * Get method from tClass who is annoted by annotation
     *
     * @param tClass
     * @param annotation
     * @return
     */
    public static Set<Method> getAnnotedMethodFromClass(Class tClass, Class annotation) {
        Set<Method> methodList = new HashSet<>();

        for (Method method : tClass.getMethods()) {
            if (method.getAnnotation(annotation) != null) {
                methodList.add(method);
            }
        }

        return methodList;
    }

}
