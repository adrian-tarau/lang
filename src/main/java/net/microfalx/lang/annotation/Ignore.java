package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation used to mark an annotated element to be ignored by element processors.
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Ignore {
}
