package net.microfalx.lang.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation that provides an order for an annotated element.
 * <p>
 * Elements will be sorted ascending in the collection based on a given order. If the order is missing, the item will receive the next available order in the collection.
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
@Inherited
public @interface Order {

    /**
     * Returns the order of the element.
     *
     * @return a positive integer
     */
    int value() default NORMAL;

    /**
     * A predefined order (large negative value) which puts the element before all elements. Should be used as a "process" and ideally only one such item
     * should exit, which would give some defaults
     */
    int BEFORE = -10000;

    /**
     * Low priority
     */
    int HIGH = 0;

    /**
     * Normal priority
     */
    int NORMAL = 1000;

    /**
     * High priority
     */
    int LOW = 2000;

    /**
     * A predefined order (large positive value) which puts the element after all "other" elements. This should be used as "if nothing else available, pick me"
     */
    int AFTER = 10000;

}
