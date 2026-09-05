package net.microfalx.lang.service;

import net.microfalx.lang.*;
import net.microfalx.lang.annotation.Description;
import net.microfalx.lang.annotation.Name;

import static net.microfalx.lang.StringUtils.EMPTY_STRING;
import static net.microfalx.lang.StringUtils.replaceFirst;

/**
 * Base class for simple application services.
 * <p>
 * If a service implements {@link Initializable} it will be automatically initialized when the service is looked
 * up for the first.
 * Similar, if the service implemented {@link Releasable} the service (resources) is destroyed when the JVM is shutdown.
 * <p>
 * If a service implement {@link Lifecycle} it will be automatically started when the service is looked up for the
 * first time and stopped when the JVM is shutdown (before it is destroyed).
 * <p>
 * A service reports what happens to it with {@link #report(Event)}; the {@link ServiceLocator} translates these
 * events into the {@link Statistics} of the service, which are later used to produce reports, and also forwards
 * them (along with the service start/stop lifecycle) to any registered {@link Listener}.
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
            String name = StringUtils.beautifyCamelCase(getClass().getSimpleName());
            return replaceFirst(name, "Impl", EMPTY_STRING);
        }
    }

    @Override
    default String getDescription() {
        Description descriptionAnnot = AnnotationUtils.getAnnotation(this, Description.class);
        if (descriptionAnnot != null) {
            return descriptionAnnot.value();
        } else {
            return EMPTY_STRING;
        }
    }

    /**
     * Reports an event about this service.
     * <p>
     * The event is applied to the statistics of the service with a value of one, which increments the
     * counters changed by the event.
     *
     * @param event the event
     * @see ServiceLocator#report(Service, Event)
     */
    default void report(Event event) {
        ServiceLocator.report(this, event);
    }

    /**
     * Reports an event about this service.
     * <p>
     * The value carries how much the event changes the statistics: the amount added to the counters changed
     * by the event or the new value of a gauge (see {@link Event#isGauge()}).
     *
     * @param event the event
     * @param value the value carried by the event
     * @see ServiceLocator#report(Service, Event, long)
     */
    default void report(Event event, long value) {
        ServiceLocator.report(this, event, value);
    }

    /**
     * Returns the statistics collected out of the events reported by this service.
     *
     * @return a non-null instance
     * @see ServiceLocator#getStatistics(Service)
     */
    default Statistics<? extends Service> getStatistics() {
        return ServiceLocator.getStatistics(this);
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

    /**
     * An enum describing the events reported by a service.
     * <p>
     * Most events are counters and increment the statistics with the value carried by the event (one by default);
     * a few of them are gauges and change the statistics to the value carried by the event. An event can change
     * more than one metric: a task which was started, for example, is not pending anymore.
     */
    enum Event {

        /**
         * The service generated a warning.
         */
        WARNING,

        /**
         * The service generated an error.
         */
        ERROR,

        /**
         * The service completed an operation successfully.
         */
        SUCCESS,

        /**
         * The service failed to complete an operation.
         */
        FAILURE,

        /**
         * A task owned by the service was scheduled and waits to be executed.
         */
        TASK_SCHEDULED,

        /**
         * A task owned by the service started to execute (and it is not pending anymore).
         */
        TASK_STARTED,

        /**
         * A task owned by the service completed successfully (and it is not running anymore).
         */
        TASK_SUCCEEDED,

        /**
         * A task owned by the service failed to complete (and it is not running anymore).
         */
        TASK_FAILED,

        /**
         * A thread used by the service was started.
         */
        THREAD_STARTED,

        /**
         * A thread used by the service was stopped.
         */
        THREAD_STOPPED,

        /**
         * The memory used by the service was measured, the value of the event is the memory usage, in bytes.
         */
        MEMORY_USAGE(true),

        /**
         * The threads used by the service were counted, the value of the event is the number of threads.
         */
        THREAD_COUNT(true);

        private final boolean gauge;

        Event() {
            this(false);
        }

        Event(boolean gauge) {
            this.gauge = gauge;
        }

        /**
         * Returns whether the event carries the new value of a metric instead of an amount added to a metric.
         *
         * @return {@code true} if the event is a gauge, {@code false} if the event is a counter
         */
        public boolean isGauge() {
            return gauge;
        }
    }

    /**
     * A listener notified about the lifecycle and the events of a service.
     * <p>
     * Listeners are discovered with the JDK {@link java.util.ServiceLoader} and with the
     * {@link net.microfalx.lang.annotation.Provider} pattern (see
     * {@link net.microfalx.lang.ClassUtils#resolveProviderInstances(Class)}), and can also be registered directly
     * with {@link ServiceLocator#addListener(Listener)}. All methods are called with the service instance they
     * refer to and have a default (empty) implementation, so a listener only needs to override what it cares about.
     * <p>
     * Listeners are called synchronously, from the thread which changed the service (started it, stopped it or
     * reported the event); an implementation should not block.
     */
    interface Listener {

        /**
         * Notifies the listener that a service was started (looked up or registered for the first time).
         *
         * @param service the service
         */
        default void onServiceStarted(Service service) {
        }

        /**
         * Notifies the listener that a service was stopped (shut down).
         *
         * @param service the service
         */
        default void onServiceStopped(Service service) {
        }

        /**
         * Notifies the listener that a service reported an event.
         *
         * @param service the service which reported the event
         * @param event   the event
         * @param value   the value carried by the event
         * @see Service#report(Event, long)
         */
        default void onServiceEvent(Service service, Event event, long value) {
        }
    }

    /**
     * An interface describing the statistics of a service.
     * <p>
     * The statistics are a read-only view over the metrics collected out of the events reported with
     * {@link Service#report(Event)}, and they are owned by the {@link ServiceLocator}.
     */
    interface Statistics<S extends Service> extends Identifiable<String>, Nameable, Descriptable {

        /**
         * Returns the service for which the statistics are collected.
         *
         * @return a non-null instance
         */
        S getService();

        /**
         * Returns the class name of the service for which the statistics are collected.
         *
         * @return a non-null instance
         */
        String getClassName();

        /**
         * Returns the memory usage of the service for which the statistics are collected.
         *
         * @return a positive number or 0 if the memory usage cannot be determined
         */
        long getMemoryUsage();

        /**
         * Returns the number of warnings generated by this service.
         *
         * @return a positive number or 0 if no warnings were generated
         */
        int getWarningCount();

        /**
         * Returns the number of errors generated by this service.
         *
         * @return a positive number or 0 if no errors were generated
         */
        int getErrorCount();

        /**
         * Returns the number of successful operations performed by this service.
         *
         * @return a positive number or 0 if no successful operations were performed
         */
        int getSuccessCount();

        /**
         * Returns the number of failed operations performed by this service.
         *
         * @return a positive number or 0 if no successful operations were performed
         */
        int getFailedCount();

        /**
         * Returns the number of running of tasks owned by this service.
         *
         * @return a positive number or 0 if no tasks are running
         */
        int getTaskRunningCount();

        /**
         * Returns the number of pending tasks owned by this service.
         *
         * @return a positive number or 0 if no tasks are pending
         */
        int getTaskPendingCount();

        /**
         * Returns the number of threads used by this service.
         *
         * @return a positive number or 0 if no threads are used
         */
        int getThreadCount();
    }
}
