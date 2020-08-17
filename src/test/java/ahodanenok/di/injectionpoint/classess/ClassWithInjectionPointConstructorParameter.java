package ahodanenok.di.injectionpoint.classess;

import ahodanenok.di.interceptor.InjectionPoint;

import javax.inject.Inject;

public class ClassWithInjectionPointConstructorParameter {

    private InjectionPoint injectionPoint;

    @Inject
    public ClassWithInjectionPointConstructorParameter(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }
}
