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
@Retention(RUNTIME)
@Target(value = {TYPE, METHOD})
@Documented
public @interface Asynchronous {

    /**
     * Returns whether the annotated element should be processed asynchronously.
     *
     * @return {@code true} for asynchronous, {@code false} otherwise
     */
    boolean value() default true;
}

