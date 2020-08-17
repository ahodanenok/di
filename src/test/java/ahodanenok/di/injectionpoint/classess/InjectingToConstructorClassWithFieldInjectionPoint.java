package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToConstructorClassWithFieldInjectionPoint {

    private ClassWithInjectionPointField obj;

    @Inject
    public InjectingToConstructorClassWithFieldInjectionPoint(ClassWithInjectionPointField obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointField getObj() {
        return obj;
    }
}
