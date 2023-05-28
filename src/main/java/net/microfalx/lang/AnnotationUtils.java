package net.microfalx.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around annotations.
 */
public class AnnotationUtils {

    /**
     * Extracts the name associated with the annotated element.
     *
     * @param element the element (field, method, class)
     * @return the name, null if the name is not defined.
     */
    public static String getName(AnnotatedElement element, String overrideName) {
        requireNonNull(element);

        if (overrideName != null && !overrideName.isEmpty()) return overrideName;

        Name nameAnnot = element.getAnnotation(Name.class);
        if (nameAnnot != null && !nameAnnot.value().isEmpty()) {
            return nameAnnot.value();
        }

        if (element instanceof Class) {
            return ((Class<?>) element).getSimpleName();
        } else if (element instanceof Field) {
            return ((Field) element).getName();
        } else {
            return null;
        }
    }

    /**
     * Extracts the identifier associated with the annotated element.
     *
     * @param element the element (field, method, class)
     * @return the identifier, null if the identifier is not defined.
     */
    public static String getId(AnnotatedElement element, String overrideId) {
        requireNonNull(element);

        if (overrideId != null && !overrideId.isEmpty()) {
            return overrideId;
        }

        Id idAnnot = element.getAnnotation(Id.class);
        if (idAnnot != null && !idAnnot.value().isEmpty()) {
            return idAnnot.value();
        }

        if (element instanceof Class) {
            return ((Class<?>) element).getName().toLowerCase();
        } else if (element instanceof Field) {
            return ((Field) element).getName().toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * Extracts the glue string associated with the annotated element.
     *
     * @param element the element (field, method, class)
     * @return the name, null if the name is not available.
     */
    public static String getGlue(AnnotatedElement element) {
        requireNonNull(element);

        Glue glueAnnot = element.getAnnotation(Glue.class);
        if (glueAnnot != null && !glueAnnot.value().isEmpty()) {
            return glueAnnot.value();
        }

        return ", ";
    }

    /**
     * Returns the given annotation on the given object, walking up the class hierarchy.
     * <p>
     * The method does not scan annotations implemented by classes in the hierarchy.
     *
     * @param object          object on which to get the given annotation class
     * @param annotationClass annotation class to get
     * @return annotation class, or <code>null</code> if no such annotation is found in the class hierarchy
     */
    public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotationClass) {
        return getAnnotation(object, annotationClass, false);
    }

    /**
     * Returns the given annotation on the given object, walking up the class hierarchy.
     *
     * @param object            object on which to get the given annotation class
     * @param annotationClass   annotation class to get
     * @param includeInterfaces <code>true</code> to scan the annotations too, <code>false</code> otherwise
     * @return annotation class, or <code>null</code> if no such annotation is found in the class hierarchy
     */
    public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotationClass, boolean includeInterfaces) {
        if (object == null) return null;
        if (object instanceof Class) {
            return getAnnotation((Class<?>) object, annotationClass, includeInterfaces);
        } else if (object instanceof Method) {
            return getAnnotation((Method) object, annotationClass, includeInterfaces);
        } else {
            return getAnnotation(object.getClass(), annotationClass, includeInterfaces);
        }
    }

    /**
     * Returns the given annotation on the given object, walking up the class hierarchy.
     *
     * @param object          object on which to get the given annotation class (class/method/field or an object)
     * @param annotationClass annotation class to get
     * @return annotation class, or <code>null</code> if no such annotation is found in the class hierarchy
     */
    public static <A extends Annotation> Collection<A> getAnnotations(Object object, Class<A> annotationClass) {
        if (object == null) {
            return null;
        }
        if (object instanceof Class) {
            return getAnnotations((Class<?>) object, annotationClass);
        } else if (object instanceof Method) {
            return getAnnotations((Method) object, annotationClass);
        } else if (object instanceof Field) {
            return getAnnotations((Field) object, annotationClass);
        } else {
            return getAnnotations(object.getClass(), annotationClass);
        }
    }

    /**
     * Returns the given annotation on the given class, walking up the class hierarchy.
     * <p>
     * The method does not scan annotations implemented by classes in the hierarchy.
     *
     * @param clazz           the class on which to get the given annotation class
     * @param annotationClass annotation class to get
     * @return annotation class, or <code>null</code> if no such annotation is found in the class hierarchy
     */
    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
        return getAnnotation(clazz, annotationClass, false);
    }

    /**
     * Returns the given annotation on the given class, walking up the class hierarchy.
     *
     * @param clazz             the class on which to get the given annotation class
     * @param annotationClass   annotation class to get
     * @param includeInterfaces <code>true</code> to scan the annotations too, <code>false</code> otherwise
     * @return annotation class, or <code>null</code> if no such annotation is found in the class hierarchy
     */
    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass, boolean includeInterfaces) {
        requireNonNull(clazz);

        A annotation;
        while (clazz != null && !Object.class.equals(clazz)) {
            annotation = clazz.getAnnotation(annotationClass);
            if (annotation != null) return annotation;
            if (includeInterfaces) {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    annotation = anInterface.getAnnotation(annotationClass);
                    if (annotation != null) return annotation;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }


}
