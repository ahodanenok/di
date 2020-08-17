package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToMethodClassWithMethodInjectionPoint {

    private ClassWithInjectionPointMethod obj;

    @Inject
    public void setObj(ClassWithInjectionPointMethod obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointMethod getObj() {
        return obj;
    }
}
