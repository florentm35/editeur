package fr.florent.editor.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {
    String value() default "File";
    int priority() default 500;

    int MAX_LEVEL = 1000;
}
