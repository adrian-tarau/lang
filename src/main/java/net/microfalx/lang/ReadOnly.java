package net.microfalx.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation use to control if an annotated element is read/write (can be changed) or read-only.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ReadOnly {

    /**
     * Return if the annotated element/model/entity should be read-only.
     *
     * @return the constraints
     */
    boolean value() default false;

    /**
     * Return when the annotated element/model/entity should be read-only.
     *
     * @return the constraints
     */
    Mode[] modes() default {Mode.ADD, Mode.EDIT};

    /**
     * An enum to provide the details on the constraint
     */
    enum Mode {

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
