package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides an I18n key (or prefix used to create a key) used for internationalization.
 * <p>
 * In most cases the complete key is extracted out of fields and walking the class hierarchy.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, FIELD})
@Inherited
public @interface I18n {

    /**
     * Return the i18n key or prefix.
     *
     * @return a non-null instance
     */
    String value();
}
