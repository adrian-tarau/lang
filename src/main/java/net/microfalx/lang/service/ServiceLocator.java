package net.microfalx.lang.service;

import net.microfalx.lang.ClassUtils;
import net.microfalx.lang.Initializable;
import net.microfalx.lang.Releasable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.ClassUtils.isSubClassOf;

/**
 * A factory which provides implementations of services.
 * <p>
 * The factory uses the JDK {@link ServiceLoader} and {@link ClassUtils#resolveProviderInstances(Class)}
 * to discover the implementations of services.
 */
public class ServiceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLocator.class);

    private static final Map<Class<?>, Service> services = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Service> serviceImplementations = new ConcurrentHashMap<>();
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * Shuts down all services. This method should be called when the application is shutting down to ensure
     * that all services are properly stopped.
     */
    public static void shutdown() {
        synchronized (ServiceLocator.class) {
            LOGGER.info("Shutting down services");
            serviceImplementations.values().forEach(ServiceLocator::stopService);
            serviceImplementations.values().forEach(ServiceLocator::destroyService);
            serviceImplementations.clear();
        }
    }

    /**
     * Shuts down a service.
     *
     * @param serviceClass the class of the service to shut down.
     * @param <S>          the type of the service
     */
    public static <S extends Service> void shutdown(Class<S> serviceClass) {
        requireNonNull(serviceClass);
        synchronized (ServiceLocator.class) {
            LOGGER.info("Shutting down service {}", ClassUtils.getName(serviceClass));
            Service service = serviceImplementations.remove(serviceClass);
            if (service != null) {
                try {
                    if (service instanceof Releasable) {
                        try {
                            ((Releasable) service).release();
                        } catch (Exception e) {
                            LOGGER.atWarn().setCause(e).log("Error while releasing service {}", ClassUtils.getName(serviceClass));
                        }
                    }
                    stopService(service);
                } catch (Exception e) {
                    LOGGER.atWarn().setCause(e).log("Error while shutting down service {}", ClassUtils.getName(serviceClass));
                }
            }
            serviceImplementations.remove(serviceClass);
        }
    }

    /**
     * Returns a collection of all loaded services.
     *
     * @return a non-null instance
     */
    public static Collection<Service> getServices() {
        return new ArrayList<>(serviceImplementations.values());
    }

    /**
     * Checks if a service is loaded.
     *
     * @param serviceClass the class of the service to check
     * @param <S>          the type of the service
     * @return true if the service is loaded, false otherwise
     */
    public static <S extends Service> boolean isLoaded(Class<S> serviceClass) {
        requireNonNull(serviceClass);
        return services.containsKey(serviceClass);
    }

    /**
     * Registers a new service.
     *
     * @param service the service instance
     * @param <S>     the service type
     */
    @SuppressWarnings("unchecked")
    public static <S extends Service> void register(S service) {
        requireNonNull(service);
        synchronized (ServiceLocator.class) {
            ClassUtils.getInterfaces(service.getClass()).stream()
                    .filter(Service.class::isAssignableFrom)
                    .forEach(serviceClass -> services.put(serviceClass, service));
            initialize(service, (Class<S>) service.getClass());
            serviceImplementations.put(service.getClass(), service);
        }
    }

    /**
     * Looks up a service.
     * <p>
     * The locator uses the Java {@link ServiceLoader} mechanism to load implementations of services and
     * {@link ClassUtils#resolveProviderInstances(Class)}. If a service has already been loaded (and initialized),
     * it will be returned from the cache.
     *
     * @param serviceClass the class of the service to load
     * @param <S>          the type of the service
     * @return an instance of the requested service
     */
    @SuppressWarnings("unchecked")
    public static <S extends Service> S lookup(Class<S> serviceClass) {
        requireNonNull(serviceClass);
        synchronized (ServiceLocator.class) {
            S service = (S) services.get(serviceClass);
            if (service == null) {
                service = doLoad(serviceClass);
                register(service);
            }
            return service;
        }
    }

    static void startService(Object service) {
        if (service instanceof Lifecycle) {
            try {
                ((Lifecycle) service).start();
            } catch (Exception e) {
                LOGGER.atError().setCause(e).log("Failed to stop service {}",
                        ClassUtils.getName(service));
            }
        }
    }

    static void stopService(Object service) {
        if (service instanceof Lifecycle) {
            try {
                ((Lifecycle) service).stop();
            } catch (Exception e) {
                LOGGER.atWarn().setCause(e).log("Failed to stop service {}",
                        ClassUtils.getName(service));
            }
        }
    }

    static void destroyService(Object service) {
        if (service instanceof Releasable) {
            try {
                ((Releasable) service).release();
            } catch (Exception e) {
                LOGGER.atWarn().setCause(e).log("Failed to release service {}",
                        ClassUtils.getName(service));
            }
        }
    }

    private static <S extends Service> S doLoad(Class<S> serviceClass) {
        LOGGER.info("Loading service {}", ClassUtils.getName(serviceClass));
        initShutdown();
        Collection<S> services = new ArrayList<>();
        ServiceLoader.load(serviceClass).stream().forEach(s -> services.add(s.get()));
        services.addAll(ClassUtils.resolveProviderInstances(serviceClass));
        if (services.size() > 1) {
            throw new ServiceException("Multiple service implementations located for type " + serviceClass.getName()
                    + ": " + services.stream().map(ClassUtils::getName).collect(Collectors.joining(",")));
        } else if (!services.isEmpty()) {
            return services.iterator().next();
        } else {
            throw new ServiceException("A service of type " + serviceClass.getName() + " could not be found");
        }
    }

    private static void initShutdown() {
        if (initialized.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(ServiceLocator::shutdown));
        }
    }

    private static <S extends Service> void initialize(S service, Class<S> serviceClass) {
        if (!isSubClassOf(service, serviceClass)) {
            throw new ServiceException("The service " + ClassUtils.getName(service) + " is not a subclass of "
                    + ClassUtils.getName(serviceClass));
        }
        if (service instanceof Initializable) ((Initializable) service).initialize();
        startService(service);
    }


}
