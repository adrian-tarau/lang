package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a description for the annotated element.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD, FIELD, PARAMETER})
@Inherited
public @interface Description {

    /**
     * Returns the description.
     * <p>
     * The application will allow for the use of place-holders the format is {@code {NAME}}. Each annotated object
     * will support different placeholders but these are the most common ones: name, group, etc.
     *
     * @return a non-null string with the description
     */
    String value();
}
