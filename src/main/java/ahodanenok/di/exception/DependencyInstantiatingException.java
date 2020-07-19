package ahodanenok.di.exception;

import ahodanenok.di.DependencyIdentifier;

public class DependencyInstantiatingException extends DependencyInjectionException {

    private DependencyIdentifier<?> id;

    public DependencyInstantiatingException(DependencyIdentifier<?> id, String reason) {
        super(String.format("Dependency '%s' can't be instantiated: %s", id, reason));
        this.id = id;
    }

    public DependencyIdentifier<?> getId() {
        return id;
    }
}
