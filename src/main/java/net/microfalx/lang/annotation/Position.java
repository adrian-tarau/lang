package net.microfalx.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the position (within a parent/visual element/etc) for the annotated element.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Position {

    /**
     * Returns the position
     *
     * @return a positive integer
     */
    int value();
}
