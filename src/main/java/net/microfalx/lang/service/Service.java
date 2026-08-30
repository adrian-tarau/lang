package net.microfalx.lang.service;

import net.microfalx.lang.*;
import net.microfalx.lang.annotation.Description;
import net.microfalx.lang.annotation.Name;

/**
 * Base class for simple application services.
 * <p>
 * If a service implements {@link Initializable} it will be automatically initialized when the service is looked
 * up for the first.
 * Similar, if the service implemented {@link Releasable} the service (resources) is destroyed when the JVM is shutdown.
 * <p>
 * If a service implement {@link Lifecycle} it will be automatically started when the service is looked up for the
 * first time and stopped when the JVM is shutdown (before it is destroyed).
 */
public interface Service extends Identifiable<String>, Nameable, Descriptable {

    @Override
    default String getId() {
        return Hashing.hash(getClass().getName());
    }

    @Override
    default String getName() {
        Name nameAnnot = AnnotationUtils.getAnnotation(this, Name.class);
        if (nameAnnot != null) {
            return nameAnnot.value();
        } else {
            return StringUtils.beautifyCamelCase(getClass().getSimpleName());
        }
    }

    @Override
    default String getDescription() {
        Description descriptionAnnot = AnnotationUtils.getAnnotation(this, Description.class);
        if (descriptionAnnot != null) {
            return descriptionAnnot.value();
        } else {
            return StringUtils.beautifyCamelCase(getClass().getSimpleName());
        }
    }

    /**
     * Looks up a service.
     *
     * @param serviceClass the service class
     * @param <T>          the service type
     * @return the service implementation
     * @see ServiceLocator#lookup(Class)
     */
    static <T extends Service> T lookup(Class<T> serviceClass) {
        return ServiceLocator.lookup(serviceClass);
    }
}
