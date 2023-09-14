package net.microfalx.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the height (within a parent/visual element/etc) for the annotated element.
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface Height {

    /**
     * Returns the height in rows.
     *
     * @return the height in rows, -1 for not defined
     */
    int rows() default -1;

    /**
     * Returns the height in screen units.
     *
     * @return the height, empty if not set
     */
    String value() default "";

    /**
     * Returns the minimum width in screen units.
     * @return the minimum width, empty if not set
     */
    String min() default "";

    /**
     * Returns the maximum width in screen units.
     * @return the maximum width, empty if not set
     */
    String max() default "";
}
