package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Visible {

    /**
     * Return if the annotated element/model/entity should be read-only.
     *
     * @return the constraints
     */
    boolean value() default true;

    /**
     * Return when the annotated element/model/entity should be read-only.
     *
     * @return the constraints
     */
    Mode[] modes() default {Mode.BROWSE, Mode.ADD, Mode.EDIT, Mode.VIEW};

    /**
     * Returns a collection of fields to apply the annotation to when the annotation is used with a type.
     *
     * @return a non-null instance
     */
    String[] fieldNames() default {};

    /**
     * An enum to provide the details on the constraint
     */
    enum Mode {

        /**
         * The annotated element is visible only when it is displayed in the grid.
         */
        BROWSE,

        /**
         * The annotated element is visible only when an "view" operation is performed.
         */
        VIEW,

        /**
         * The annotated element is visible only when an "add" operation is performed.
         */
        ADD,

        /**
         * The annotated element is visible only when the "edit" operation is performed.
         */
        EDIT
    }
}
