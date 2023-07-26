package net.microfalx.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the width (within a parent/visual element/etc) for the annotated element.
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface Width {

    /**
     * Returns the width in columns.
     *
     * @return the width in columns, -1 for not defined
     */
    int columns() default -1;

    /**
     * Returns the width in screen units.
     *
     * @return the width, empty if not set
     */
    String value() default "";
}
