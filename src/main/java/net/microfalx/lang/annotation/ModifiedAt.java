package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates which annotated element (field) holds the timestamp when the model was modified.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface ModifiedAt {
}
