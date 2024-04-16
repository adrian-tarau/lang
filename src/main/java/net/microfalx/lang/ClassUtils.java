package net.microfalx.lang;

import net.microfalx.lang.annotation.Provider;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableList;
import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.ArgumentUtils.requireNotEmpty;
import static net.microfalx.lang.StringUtils.EMPTY_STRING;

/**
 * Various utilities around classes and class loaders.
 */
public class ClassUtils {

    private static final Map<Class<?>, Collection<?>> providers = new ConcurrentHashMap<>();

    /**
     * Returns the class name.
     *
     * @param cls the class for which to get the class name; may be null
     * @return the class name or the empty string in case the argument is {@code null}
     */
    public static String getName(final Class<?> cls) {
        return getName(cls, EMPTY_STRING);
    }

    /**
     * Returns the class name.
     *
     * @param cls          the class for which to get the class name; may be null
     * @param defaultValue the return value if the argument {@code cls} is {@code null}
     * @return the class name or {@code valueIfNull}
     */
    public static String getName(final Class<?> cls, final String defaultValue) {
        return cls == null ? defaultValue : cls.getName();
    }

    /**
     * Returns the class name of an object.
     *
     * @param object the object for which to get the class name; may be null
     * @return the class name or the empty String
     */
    public static String getName(Object object) {
        return getName(object, EMPTY_STRING);
    }

    /**
     * Returns the class name of an object.
     *
     * @param object       the object for which to get the class name; may be null
     * @param defaultValue the value to return if {@code object} is {@code null}
     * @return the class name or {@code valueIfNull}
     */
    public static String getName(Object object, final String defaultValue) {
        return object == null ? defaultValue : object.getClass().getName();
    }

    /**
     * Returns the class name of an object.
     *
     * @param object the object for which to get the class name; may be null
     * @return the class name or the empty String
     */
    public static String getSimpleName(Object object) {
        return getSimpleName(object, EMPTY_STRING);
    }

    /**
     * Returns the class name of an object.
     *
     * @param object       the object for which to get the class name; may be null
     * @param defaultValue the value to return if {@code object} is {@code null}
     * @return the class name or {@code valueIfNull}
     */
    public static String getSimpleName(Object object, final String defaultValue) {
        return object == null ? defaultValue : object.getClass().getName();
    }

    /**
     * Returns the class name of an object.
     *
     * @param className the class name as a string; may be null
     * @return the class name or the empty String
     */
    public static String getSimpleName(String className) {
        if (className == null) return null;
        int lastIndex = className.lastIndexOf('.');
        return lastIndex >= 0 ? className.substring(lastIndex + 1) : className;
    }

    /**
     * Returns whether the object is a subclass of a given superclass.
     *
     * @param object     an object
     * @param superClass a super class
     * @return {@code true} if a subclass, {@code false} otherwise
     */
    public static boolean isSubClassOf(Object object, Class<?> superClass) {
        return (object == null) ? false : isSubClassOf(object.getClass(), superClass);
    }

    /**
     * Returns if clazz is a subclass of superclass
     *
     * @param clazz      a clazz
     * @param superClazz a super class
     * @return true if subclass
     */
    public static boolean isSubClassOf(Class<?> clazz, Class<?> superClazz) {
        requireNonNull(clazz);
        requireNonNull(superClazz);
        return superClazz.isAssignableFrom(clazz);
    }

    /**
     * Returns whether the object represents a class is part of the JDK.
     *
     * @param value the object
     * @return {@code true} if a JDK class, {@code false} otherwise
     */
    public static boolean isJdkClass(Object value) {
        return value != null && isJdkClass(value.getClass());
    }

    /**
     * Returns whether the class is part of the JDK.
     *
     * @param clazz the class to validate
     * @return {@code true} if a JDK class, {@code false} otherwise
     */
    public static boolean isJdkClass(Class<?> clazz) {
        requireNonNull(clazz);
        return clazz.isPrimitive() || StringUtils.EMPTY_STRING.getClass().getClassLoader() == clazz.getClassLoader();
    }

    /**
     * Returns whether the class can be instantiated.
     *
     * @param clazz the class to validate
     * @return {@code true} if an object can be created,  {@code false} otherwise
     */
    public static boolean canInstantiate(Class<?> clazz) {
        if (clazz == null) return false;
        return !(clazz.isInterface() || ((clazz.getModifiers() & Modifier.ABSTRACT) != 0));
    }

    /**
     * Returns a collection of provider classes for a given type.
     *
     * @param providerClass the provider class
     * @param <T>           the provider type
     * @return a collection of provider classes
     * @see Provider
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<Class<T>> resolveProviders(Class<T> providerClass) {
        requireNotEmpty(providerClass);
        Collection cachedProviders = ClassUtils.providers.get(providerClass);
        if (cachedProviders != null) return cachedProviders;
        Collection<Class<T>> providerClasses = new HashSet<>();
        Iterable<Class<?>> scannedProviderClasses = ClassIndex.getAnnotated(Provider.class);
        for (Class<?> scannedProviderClass : scannedProviderClasses) {
            if (ClassUtils.isSubClassOf(scannedProviderClass, providerClass)) {
                providerClasses.add((Class<T>) scannedProviderClass);
            }
        }
        List<Class<T>> orderedProviders = new ArrayList<>(providerClasses);
        AnnotationUtils.sort(orderedProviders);
        ClassUtils.providers.put(providerClass, orderedProviders);
        return orderedProviders;
    }

    /**
     * Returns a collection of providers for a given type.
     *
     * @param providerClass the provider class
     * @param <T>           the provider type
     * @return a collection of provider classes
     * @see Provider
     */
    @SuppressWarnings({"unchecked", "CastCanBeRemovedNarrowingVariableType"})
    public static <T> Collection<T> resolveProviderInstances(Class<T> providerClass) {
        requireNotEmpty(providerClass);
        Collection<T> providerInstances = new ArrayList<>();
        Collection<Class<T>> providerClasses = resolveProviders(providerClass);
        for (Class<?> providerClassLocated : providerClasses) {
            if (!canInstantiate(providerClassLocated)) continue;
            try {
                providerInstances.add(create((Class<T>) providerClassLocated));
            } catch (Exception e) {
                // ignore this for now, maybe report somewhere
            }
        }
        return providerInstances;
    }

    /**
     * Clears all caches.
     */
    public void clearCaches() {
        providers.clear();
    }

    /**
     * Returns the parametrized types used in class signature.
     *
     * @param clazz the class
     * @return a collection with classes
     */
    public static List<Class<?>> getClassParametrizedTypes(Class<?> clazz) {
        requireNonNull(clazz);
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) return Collections.emptyList();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        List<Class<?>> classes = new ArrayList<>();
        for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
            if (typeArgument instanceof Class) {
                classes.add((Class<?>) typeArgument);
            } else if (typeArgument instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) typeArgument).getRawType();
                if (rawType instanceof Class) classes.add((Class<?>) rawType);
            }
        }
        return unmodifiableList(classes);
    }

    /**
     * Returns the parametrized type used in class signature.
     *
     * @param clazz the class
     * @param index the index of the parametrized type
     * @return the type, null if the class is not parametrized, or there is no parametrized type at the given index
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassParametrizedType(Class<?> clazz, int index) {
        List<Class<?>> parametrizedTypes = getClassParametrizedTypes(clazz);
        if (index < parametrizedTypes.size()) {
            return (Class<T>) parametrizedTypes.get(index);
        }
        return null;
    }

    /**
     * Returns the first parameterized type used in class signature.
     *
     * @param clazz the class
     * @param type  the expected super type
     * @return the type, null if none is found
     */
    public static Class<?> getClassParametrizedType(Class<?> clazz, Class<?> type) {
        requireNonNull(clazz);
        List<Class<?>> existingTypes = getClassParametrizedTypes(clazz);
        for (Class<?> existingType : existingTypes) {
            if (ClassUtils.isSubClassOf(existingType, type)) {
                return existingType;
            }
        }
        return null;
    }

    /**
     * Returns an instance of an object with a given class.
     *
     * @param clazz the class
     * @param <T>   the object type
     * @return a non-null instance
     */
    public static <T> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return ExceptionUtils.throwException(e);
        }
    }
}
