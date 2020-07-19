package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;

public class DependencyInstanceValue<T> implements DependencyValue<T> {

    private DependencyIdentifier<T> id;
    private Provider<? extends T> instance;
    private ScopeIdentifier scope;

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> DependencyInstanceValue(V instance) {
        this(instance != null ? DependencyIdentifier.of((Class<T>) instance.getClass()) : null, instance);
    }

    // todo: read qualifiers from instance class?

    public <V extends T> DependencyInstanceValue(DependencyIdentifier<T> id, V instance) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }

        this.id = id;
        this.scope = ScopeIdentifier.of(Singleton.class);
        this.instance = () -> instance;
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public Provider<? extends T> provider() {
        return instance;
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }
}
