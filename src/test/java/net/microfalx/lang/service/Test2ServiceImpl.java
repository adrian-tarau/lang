package net.microfalx.lang.service;

import net.microfalx.lang.annotation.DependsOn;
import net.microfalx.lang.annotation.Provider;

@Provider
@DependsOn(classes = Test1Service.class)
public class Test2ServiceImpl implements Test2Service, Service.Lifecycle {

    @Override
    public void start() {

    }
}
