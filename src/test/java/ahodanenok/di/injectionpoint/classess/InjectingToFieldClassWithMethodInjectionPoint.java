package ahodanenok.di.injectionpoint.classess;

import javax.inject.Inject;

public class InjectingToFieldClassWithMethodInjectionPoint {

    @Inject
    private ClassWithInjectionPointMethod field;

    public ClassWithInjectionPointMethod getField() {
        return field;
    }
}
