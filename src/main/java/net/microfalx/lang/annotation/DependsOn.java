package net.microfalx.lang.annotation;

import java.lang.annotation.*;

/**
 * Specifies the classes on which the current class depends. Any class specified are validated before the
 * class is loaded/used.
 * <p>
 * The annotation can be used to decide if a class can be loaded or if some classes need to be loaded first.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependsOn {

    /**
     * Returns a collection of class names on which the current class depends.
     * <p>
     * This method should be used when dependent classes can be missing at runtime.
     *
     * @return a non-null array of classes
     */
    String[] value() default {};

    /**
     * Returns a collection of classes on which the current class depends.
     * <p>
     * This method should be used when dependent classes are guaranteed to be present at runtime.
     *
     * @return a non-null array of classes
     */
    Class<?>[] classes() default {};
}
