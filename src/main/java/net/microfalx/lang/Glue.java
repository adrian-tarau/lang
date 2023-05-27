package net.microfalx.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
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
@Inherited
public @interface Glue {

    /**
     * Returns the string used to glue multiple values.
     *
     * @return a non-null string
     */
    String value() default ", ";
}
