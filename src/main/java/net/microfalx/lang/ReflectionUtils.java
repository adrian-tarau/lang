package net.microfalx.lang;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableList;

/**
 * Various reflection utilities.
 */
public class ReflectionUtils {

    private static final Map<String, SoftReference<ClassMetadata>> classToMetadata = new ConcurrentHashMap<>();

    /**
     * Returns all the non-static fields of the class.
     *
     * @return the list of fields
     */
    public static List<Field> getFields(Class<?> clazz) {
        return getClassMetadata(clazz, false).getFields();
    }

    /**
     * Returns all the methods of the class.
     *
     * @return the list of fields
     */
    public static List<Method> getMethods(Class<?> clazz) {
        return getClassMetadata(clazz, false).getMethods();
    }

    /**
     * Makes all fields and methods accessible over reflection.
     */
    public static void openAccess(Class<?> clazz) {
        openFields(clazz);
        openMethods(clazz);
    }

    /**
     * Makes all fields accessible over reflection.
     *
     * @return the list of fields
     */
    public static List<Field> openFields(Class<?> clazz) {
        return getClassMetadata(clazz, false).openFields();
    }

    /**
     * Makes all methods accessible over reflection.
     *
     * @return self
     */
    public static List<Method> openMethods(Class<?> clazz) {
        return getClassMetadata(clazz, false).openMethods();
    }

    private static ClassMetadata getClassMetadata(Class<?> clazz, boolean includeStatics) {
        String key = clazz.getName() + (includeStatics ? ":1" : ":0");
        SoftReference<ClassMetadata> reference = classToMetadata.computeIfAbsent(key, k -> new SoftReference<>(new ClassMetadata(clazz, includeStatics)));
        return reference.get();
    }


    private static class ClassMetadata {

        private final Class<?> clazz;
        private final List<Class<?>> superClasses = new ArrayList<>();

        private final List<Constructor<?>> constructors = new ArrayList<>();
        private final List<Field> fields = new ArrayList<>();
        private final Map<String, Object> fieldsByName = new HashMap<>();
        private final List<Method> methods = new ArrayList<>();
        private final Map<String, Object> methodsByName = new HashMap<>();

        private final Map<Class<? extends Annotation>, List<Field>> fieldsPerAnnotation = new HashMap<>();
        private final Map<Class<? extends Annotation>, List<Method>> methodsPerAnnotation = new HashMap<>();

        private volatile boolean fieldsOpened;
        private volatile boolean methodsOpened;
        private boolean strict;

        ClassMetadata(Class<?> clazz, boolean includeStatics) {
            this.clazz = clazz;
            initialize(includeStatics);
        }

        private void openAccess() {
            openFields();
            openMethods();
        }

        private List<Field> getFields() {
            return unmodifiableList(fields);
        }

        private List<Field> openFields() {
            if (!fieldsOpened) {
                for (Field field : fields) {
                    if (!field.isAccessible()) field.trySetAccessible();
                }
                fieldsOpened = true;
            }
            return fields;
        }

        private List<Method> getMethods() {
            return unmodifiableList(methods);
        }

        private List<Method> openMethods() {
            if (!methodsOpened) {
                for (Method method : methods) {
                    if (!method.isAccessible()) method.trySetAccessible();
                }
                methodsOpened = true;
            }
            return getMethods();
        }

        @SuppressWarnings("unchecked")
        private void registerField(Field field) {
            fields.add(field);
            String name = field.getName();
            Object value = fieldsByName.get(name);
            if (value instanceof List) {
                ((List<Field>) value).add(field);
            } else if (value instanceof Field) {
                List<Field> fields = new ArrayList<>();
                fields.add((Field) value);
                fields.add(field);
                fieldsByName.put(name, fields);
            } else {
                fieldsByName.put(name, field);
            }

        }

        @SuppressWarnings("unchecked")
        private void registerMethod(Method method) {
            methods.add(method);
            String name = method.getName();
            Object value = methodsByName.get(name);
            if (value instanceof List) {
                ((List<Method>) value).add(method);
            } else if (value instanceof Method) {
                List<Method> methods = new ArrayList<>();
                methods.add((Method) value);
                methods.add(method);
                methodsByName.put(name, methods);
            } else {
                methodsByName.put(name, method);
            }
        }

        private void initialize(boolean includeStatics) {
            constructors.addAll(Arrays.asList(clazz.getConstructors()));
            Class<?> superClass = clazz.getSuperclass();
            List<Class<?>> classesToScan = new ArrayList<>();
            while (!(superClass == null || superClass == Object.class)) {
                classesToScan.add(0, superClass);
                superClass = superClass.getSuperclass();
            }
            superClasses.addAll(classesToScan);
            classesToScan.add(clazz);
            // scan for fields and methods declared in each class
            for (Class<?> classToScan : classesToScan) {
                Field[] fields = classToScan.getDeclaredFields();
                for (Field field : fields) {
                    if (!includeStatics && Modifier.isStatic(field.getModifiers())) continue;
                    registerField(field);
                }
                Method[] declaredMethods = classToScan.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    registerMethod(declaredMethod);
                }
            }
        }
    }
}
