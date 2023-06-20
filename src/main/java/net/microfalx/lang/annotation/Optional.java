package net.microfalx.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation indicating that the type, method, field or parameter is optional. and it should not be
 * registered automatically or provided.
 * <p/>
 * Such an optional information will not result in an error if it cannot be resolved at runtime.
 */
@Documented
@Retention(value = RUNTIME)
@Target(value = {TYPE, METHOD, FIELD, PARAMETER})
@Inherited
public @interface Optional {
}
