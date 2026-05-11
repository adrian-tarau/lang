package net.microfalx.lang;

import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * An interface for objects which carries the <code>execution</code> time.
 * <p>
 * The execution aspect should not be viewed as a task execution or a life-cycle event.
 *
 * @see Timestampable
 */
public interface ExecutionAware<T extends Temporal> {

    /**
     * Returns the instant in time when the object was executed.
     *
     * @return creation time.
     */
    T getStartedAt();

    /**
     * Returns the instant in time when the object execution has stopped.
     *
     * @return creation time.
     */
    T getEndedAt();

    /**
     * Returns the time spent in execution.
     *
     * @return a non-null instance
     */
    default Duration getDuration() {
        return Duration.between(getStartedAt(), getEndedAt());
    }
}
