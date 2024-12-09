package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates which annotated field holds the principal that modified the model.
 * <p>
 * The value stored represents the username associated with the principal.
 */
@Documented
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface ModifiedBy {
}
