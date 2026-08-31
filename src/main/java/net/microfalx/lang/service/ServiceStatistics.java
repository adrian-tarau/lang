package net.microfalx.lang.service;

import lombok.ToString;
import net.microfalx.lang.ClassUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * An implementation of {@link Service.Statistics} that provides statistics for
 * a specific service.
 */
@ToString
public class ServiceStatistics<S extends Service> implements Service.Statistics<S> {

    private final S service;

    private final AtomicLong memoryUsage = new AtomicLong();
    private final AtomicInteger warningCount = new AtomicInteger();
    private final AtomicInteger errorCount = new AtomicInteger();
    private final AtomicInteger successCount = new AtomicInteger();
    private final AtomicInteger failedCount = new AtomicInteger();
    private final AtomicInteger runningCount = new AtomicInteger();
    private final AtomicInteger pendingCount = new AtomicInteger();
    private final AtomicInteger threadCount = new AtomicInteger();

    public ServiceStatistics(S service) {
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
        return runningCount.get();
    }

    @Override
    public int getTaskPendingCount() {
        return pendingCount.get();
    }

    @Override
    public int getThreadCount() {
        return threadCount.get();
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage.set(memoryUsage);
    }

    public void setWarningCount(int warningCount) {
        this.warningCount.set(warningCount);
    }

    public int incrementWarningCount() {
        return warningCount.incrementAndGet();
    }

    public void setErrorCount(int errorCount) {
        this.errorCount.set(errorCount);
    }

    public int incrementErrorCount() {
        return errorCount.incrementAndGet();
    }

    public void setSuccessCount(int successCount) {
        this.successCount.set(successCount);
    }

    public int incrementSuccessCount() {
        return successCount.incrementAndGet();
    }

    public void setFailedCount(int failedCount) {
        this.failedCount.set(failedCount);
    }

    public int incrementFailedCount() {
        return failedCount.incrementAndGet();
    }

    public void setRunningCount(int runningCount) {
        this.runningCount.set(runningCount);
    }

    public int incrementRunningCount() {
        return runningCount.incrementAndGet();
    }

    public int decrementRunningCount() {
        return runningCount.decrementAndGet();
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount.set(pendingCount);
    }

    public int incrementPendingCount() {
        return pendingCount.incrementAndGet();
    }

    public int decrementPendingCount() {
        return pendingCount.decrementAndGet();
    }

    public void setThreadCount(int threadCount) {
        this.threadCount.set(threadCount);
    }

    public int incrementThreadCount() {
        return threadCount.incrementAndGet();
    }

    public int decrementThreadCount() {
        return threadCount.decrementAndGet();
    }
}
