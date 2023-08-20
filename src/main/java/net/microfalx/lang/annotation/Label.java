package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a label (display name) for the annotated element.
 * <p>
 * This is an alternative to {@link Name} when using cannot be used due to naming conflicts.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
@Inherited
public @interface Label {

    /**
     * The label to be used instead of the default one.
     *
     * @return the label, empty to have no label
     */
    String value();

    /**
     * The icon (CSS selectors) to be used with the label.
     *
     * @return a non-empty string if an icon is provided, empty otherwise
     */
    String icon() default "";
}