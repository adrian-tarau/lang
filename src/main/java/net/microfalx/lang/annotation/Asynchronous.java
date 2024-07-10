package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify whether the annotated element should be processed asynchronously.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD})
public @interface Asynchronous {

    /**
     * Returns whether the annotated element should be processed asynchronously.
     *
     * @return {@code true} for asynchronous, {@code false} otherwise
     */
    boolean value() default true;
}
