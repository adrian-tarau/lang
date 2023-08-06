package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a string used to glue multiple values together.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD, FIELD, PARAMETER})
public @interface Glue {

    /**
     * Returns the string used to glue multiple values, added before the value.
     *
     * @return a non-null string
     */
    String value() default ", ";

    /**
     * Returns the string used to be added before the value.
     * <p>
     * This property is an alias for {@link #value()} when used with {@link #after}.
     *
     * @return the glue value, empty if not provided
     */
    String before() default "";

    /**
     * Returns the string used to be added after the value.
     *
     * @return the glue value, empty if not provided
     */
    String after() default "";
}
