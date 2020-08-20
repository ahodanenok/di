package ahodanenok.di.exception;

import ahodanenok.di.ValueSpecifier;

public class DependencyInstantiatingException extends DependencyInjectionException {

    private ValueSpecifier<?> id;

    public DependencyInstantiatingException(ValueSpecifier<?> id, Class<?> instanceClass, String reason) {
        super(String.format("Class '%s' bound to '%s' can't be instantiated: %s", instanceClass.getName(), id, reason));
        this.id = id;
    }

    public ValueSpecifier<?> getId() {
        return id;
    }
}
