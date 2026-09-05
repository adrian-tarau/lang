package net.microfalx.lang.service;

import lombok.ToString;
import net.microfalx.lang.ClassUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * An implementation of {@link Service.Statistics} which collects the metrics of a service out of the
 * {@link Service.Event events} reported to the {@link ServiceLocator}.
 * <p>
 * The metrics are updated with atomics, which makes the statistics safe to be updated from any thread. The counters
 * never go below zero, an event which decrements a metric (a task which completes without being started, for example)
 * is simply ignored.
 */
@ToString
class ServiceStatistics<S extends Service> implements Service.Statistics<S> {

    private final S service;

    private final AtomicLong memoryUsage = new AtomicLong();
    private final AtomicInteger warningCount = new AtomicInteger();
    private final AtomicInteger errorCount = new AtomicInteger();
    private final AtomicInteger successCount = new AtomicInteger();
    private final AtomicInteger failedCount = new AtomicInteger();
    private final AtomicInteger taskRunningCount = new AtomicInteger();
    private final AtomicInteger taskPendingCount = new AtomicInteger();
    private final AtomicInteger threadCount = new AtomicInteger();

    ServiceStatistics(S service) {
        requireNonNull(service);
        this.service = service;
    }

    @Override
    public String getId() {
        return service.getId();
    }

    @Override
    public String getName() {
        return service.getName();
    }

    @Override
    public String getDescription() {
        return service.getDescription();
    }

    @Override
    public S getService() {
        return service;
    }

    @Override
    public String getClassName() {
        return ClassUtils.getName(service);
    }

    @Override
    public long getMemoryUsage() {
        return memoryUsage.get();
    }

    @Override
    public int getWarningCount() {
        return warningCount.get();
    }

    @Override
    public int getErrorCount() {
        return errorCount.get();
    }

    @Override
    public int getSuccessCount() {
        return successCount.get();
    }

    @Override
    public int getFailedCount() {
        return failedCount.get();
    }

    @Override
    public int getTaskRunningCount() {
        return taskRunningCount.get();
    }

    @Override
    public int getTaskPendingCount() {
        return taskPendingCount.get();
    }

    @Override
    public int getThreadCount() {
        return threadCount.get();
    }

    /**
     * Applies an event reported by the service to the metrics changed by such an event.
     *
     * @param event the event
     * @param value the value carried by the event
     */
    void apply(Service.Event event, long value) {
        requireNonNull(event);
        switch (event) {
            case WARNING -> increment(warningCount, value);
            case ERROR -> increment(errorCount, value);
            case SUCCESS -> increment(successCount, value);
            case FAILURE -> increment(failedCount, value);
            case TASK_SCHEDULED -> increment(taskPendingCount, value);
            case TASK_STARTED -> {
                increment(taskPendingCount, -value);
                increment(taskRunningCount, value);
            }
            case TASK_SUCCEEDED -> {
                increment(taskRunningCount, -value);
                increment(successCount, value);
            }
            case TASK_FAILED -> {
                increment(taskRunningCount, -value);
                increment(failedCount, value);
            }
            case THREAD_STARTED -> increment(threadCount, value);
            case THREAD_STOPPED -> increment(threadCount, -value);
            case MEMORY_USAGE -> memoryUsage.set(Math.max(0, value));
            case THREAD_COUNT -> threadCount.set((int) Math.max(0, value));
        }
    }

    private void increment(AtomicInteger counter, long delta) {
        counter.updateAndGet(value -> (int) Math.max(0, value + delta));
    }
}
