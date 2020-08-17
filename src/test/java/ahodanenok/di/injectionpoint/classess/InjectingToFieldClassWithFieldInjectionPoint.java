package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToFieldClassWithFieldInjectionPoint {

    @Inject
    private ClassWithInjectionPointField field;

    public ClassWithInjectionPointField getField() {
        return field;
    }
}
