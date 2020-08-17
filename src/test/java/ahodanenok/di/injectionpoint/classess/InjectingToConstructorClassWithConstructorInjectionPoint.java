package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToConstructorClassWithConstructorInjectionPoint {

    private ClassWithInjectionPointConstructorParameter obj;

    @Inject
    public InjectingToConstructorClassWithConstructorInjectionPoint(ClassWithInjectionPointConstructorParameter obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointConstructorParameter getObj() {
        return obj;
    }
}
