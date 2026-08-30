package net.microfalx.lang.service;

/**
 * Provides lifecycle management for a service.
 */
public interface Lifecycle {

    /**
     * Starts the service.
     */
    void start();

    /**
     * Stops the service.
     */
    default void stop() {

    }
}
