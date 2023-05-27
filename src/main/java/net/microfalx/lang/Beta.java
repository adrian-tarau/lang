package net.microfalx.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a public API that is still in "beta".
 * <p>
 * This annotation signals that the annotated public API (package, class, method or field)
 * has not been fully stabilized yet. As such, the API is subject to backward-incompatible changes
 * (or even removal) in a future release. Development team does not make any guarantees
 * to retain backward compatibility
 * <p>
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, CONSTRUCTOR, FIELD, METHOD, TYPE})
@Documented
@Inherited
public @interface Beta {

    /**
     * Returns whether the API is exposed (in the documentation).
     *
     * @return <code>true</code> to expose the API, <code>false</code> to hide it
     */
    boolean hidden() default false;
}
