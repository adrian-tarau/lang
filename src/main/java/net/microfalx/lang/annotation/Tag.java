package net.microfalx.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation use to tag types and fields.
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface Tag {

    /**
     * A collection of tags.
     *
     * @return a non-null array
     */
    String[] value();
}
