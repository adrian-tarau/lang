package net.microfalx.lang.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation used to control if an annotated element is read/write (can be changed) or read-only.
 */
@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Inherited
public @interface ReadOnly {

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
    Mode[] modes() default {Mode.ADD, Mode.EDIT};

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
         * The annotated element is read-only only when an "add" operation is performed.
         */
        ADD,

        /**
         * The annotated element is read-only only when the "edit" operation is performed.
         */
        EDIT
    }
}
