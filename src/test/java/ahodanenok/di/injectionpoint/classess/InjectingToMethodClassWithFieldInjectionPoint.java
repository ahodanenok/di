package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToMethodClassWithFieldInjectionPoint {

    private ClassWithInjectionPointField obj;

    @Inject
    public void setObj(ClassWithInjectionPointField obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointField getObj() {
        return obj;
    }
}
