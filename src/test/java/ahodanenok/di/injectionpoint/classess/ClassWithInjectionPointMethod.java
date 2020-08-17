package ahodanenok.di.injectionpoint.classess;

import ahodanenok.di.interceptor.InjectionPoint;

import javax.inject.Inject;

public class ClassWithInjectionPointMethod {

    private InjectionPoint injectionPoint;

    @Inject
    public void setInjectionPoint(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }
}
