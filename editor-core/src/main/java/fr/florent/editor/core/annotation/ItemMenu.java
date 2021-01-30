package fr.florent.editor.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemMenu {
    String label();
    String parent() default "";
    int priority() default 500;

    int MAX_LEVEL = 1000;
}
