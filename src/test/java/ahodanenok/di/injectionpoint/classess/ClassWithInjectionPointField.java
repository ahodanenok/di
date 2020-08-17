package ahodanenok.di.injectionpoint.classess;

import ahodanenok.di.interceptor.InjectionPoint;

import javax.inject.Inject;

public class ClassWithInjectionPointField {

    @Inject
    private InjectionPoint injectionPoint;

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }
}
