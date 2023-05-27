package net.microfalx.lang;

import java.lang.annotation.*;

/**
 * Identifies the version of an API or a class in general.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Version {

    /**
     * Returns the version number
     *
     * @return a positive integer
     */
    int value() default 1;
}
