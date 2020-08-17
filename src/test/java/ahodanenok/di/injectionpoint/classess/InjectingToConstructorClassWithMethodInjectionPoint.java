package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToConstructorClassWithMethodInjectionPoint {

    private ClassWithInjectionPointMethod obj;

    @Inject
    public InjectingToConstructorClassWithMethodInjectionPoint(ClassWithInjectionPointMethod obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointMethod getObj() {
        return obj;
    }
}
