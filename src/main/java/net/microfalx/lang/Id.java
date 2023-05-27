package net.microfalx.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides an identifier for the annotated element.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD, FIELD, PARAMETER})
public @interface Id {

    /**
     * Returns the identifier given to the element.
     * <p>
     * The value might be ignored by some API and only consider the value of the annotated element (like a field becomes part of the object identifier).
     *
     * @return a non-null string with the name, empty if the value should be extracted from the annotated element (field value, class name, etc).
     */
    String value() default "";
}
