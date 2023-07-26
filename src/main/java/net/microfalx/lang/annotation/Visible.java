package net.microfalx.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
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
    Mode[] modes() default {Mode.BROWSE, Mode.ADD, Mode.EDIT};

    /**
     * An enum to provide the details on the constraint
     */
    enum Mode {

        /**
         * The annotated element is read-only only when it is displayed.
         */
        BROWSE,

        /**
         * The annotated element is read-only only when an "add" operation is performed.
         */
        ADD,

        /**
         * The annotated element is read-only only when the "edit" operation is performed.
         */
        EDIT
    }
}
