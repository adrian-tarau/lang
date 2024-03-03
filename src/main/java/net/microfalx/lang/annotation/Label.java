package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a label (display name, field name, title, etc) for the annotated element.
 * <p>
 * This is an alternative to {@link Name} when using cannot be used due to naming conflicts.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
@Inherited
public @interface Label {

    /**
     * Return the label (name/title) to be used instead of the default one.
     *
     * @return the label, empty to have no label
     */
    String value();

    /**
     * Return the icon (CSS selectors) to be used with the label.
     *
     * @return a non-empty string if an icon is provided, empty otherwise
     */
    String icon() default "";

    /**
     * Returns the label (name/title) for the grouping element.
     *
     * @return a non-empty string if there is a group, empty otherwise
     */
    String group() default "";

    /**
     * Returns whether the label has a separator between this label and next label.
     *
     * @return {@code true} if there is a separator, {@code false} otherwise
     */
    boolean separator() default false;
}
