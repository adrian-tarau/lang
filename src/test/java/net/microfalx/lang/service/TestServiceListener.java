package net.microfalx.lang.service;

import net.microfalx.lang.annotation.Provider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Provider
public class TestServiceListener implements Service.Listener {

    static final List<Service> started = new CopyOnWriteArrayList<>();
    static final List<Service> stopped = new CopyOnWriteArrayList<>();
    static final List<Object[]> events = new CopyOnWriteArrayList<>();

    static void reset() {
        started.clear();
        stopped.clear();
        events.clear();
    }

    @Override
    public void onServiceStarted(Service service) {
        started.add(service);
    }

    @Override
    public void onServiceStopped(Service service) {
        stopped.add(service);
    }

    @Override
    public void onServiceEvent(Service service, Service.Event event, long value) {
        events.add(new Object[]{service, event, value});
    }
}
