package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToFieldClassWithConstructorInjectionPoint {

    @Inject
    private ClassWithInjectionPointConstructorParameter field;

    public ClassWithInjectionPointConstructorParameter getField() {
        return field;
    }
}
