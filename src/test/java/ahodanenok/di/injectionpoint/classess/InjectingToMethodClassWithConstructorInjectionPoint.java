package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToMethodClassWithConstructorInjectionPoint {

    private ClassWithInjectionPointConstructorParameter obj;

    @Inject
    public void setObj(ClassWithInjectionPointConstructorParameter obj) {
        this.obj = obj;
    }

    public ClassWithInjectionPointConstructorParameter getObj() {
        return obj;
    }
}
