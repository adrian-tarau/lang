package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates which annotated element (field) holds the timestamp which provides a timeline for the parent.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
@Inherited
public @interface Timestamp {
}
