package net.microfalx.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a name for the annotated element.
 * <p>
 * If multiple elements are annotated, the name is created by joining all the names with {@link Glue#value()}
 * (if present) or {@link #separator()}.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD, FIELD, PARAMETER})
@Inherited
public @interface Name {

    /**
     * Returns the name given to the element.
     *
     * @return a non-null string with the name, empty if the value should be extracted from the annotated element (field value, i18n lookup, etc).
     */
    String value() default "";

    /**
     * Returns the separator appended between this name value and the previous name value.
     *
     * @return the display value separator
     */
    String separator() default ",";

    /**
     * Returns the position of the field in the object name.
     *
     * @return a positive integer
     */
    int position() default 1;

}
