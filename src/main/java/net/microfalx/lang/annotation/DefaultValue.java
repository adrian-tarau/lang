package net.microfalx.lang.annotation;

import java.lang.annotation.*;

/**
 * Defines a default value for a given parameter, method or field.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultValue {

    /**
     * The specified default value.
     *
     * @return default value.
     */
    String value();
}
