package ahodanenok.di.exception;

import ahodanenok.di.DependencyIdentifier;

public class DependencyInstantiatingException extends DependencyInjectionException {

    private DependencyIdentifier<?> id;

    public DependencyInstantiatingException(DependencyIdentifier<?> id, Class<?> instanceClass, String reason) {
        super(String.format("Class '%s' bound to '%s' can't be instantiated: %s", instanceClass.getName(), id, reason));
        this.id = id;
    }

    public DependencyIdentifier<?> getId() {
        return id;
    }
}
