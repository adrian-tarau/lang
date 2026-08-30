package net.microfalx.lang.service;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * A proxy for a service.
 */
class ServiceProxy implements Service, Lifecycle {

    private final Object service;

    public ServiceProxy(Object service) {
        requireNonNull(service);
        this.service = service;
    }

    public Object getService() {
        return service;
    }

    @Override
    public void start() {
        ServiceLocator.startService(service);
    }

    @Override
    public void stop() {
        ServiceLocator.stopService(service);
    }
}
