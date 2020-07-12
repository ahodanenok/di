package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;

public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private T instance;
    private ScopeIdentifier scope;

    public <V extends T> DependencyInstanceValue(V instance) {
        this(DependencyIdentifier.of((Class<T>) instance.getClass()), instance);
    }

    // todo: read qualifiers from instance class?

    public <V extends T> DependencyInstanceValue(DependencyIdentifier<T> id, V instance) {
        super(id);
        this.scope = ScopeIdentifier.of(Singleton.class);
        this.instance = instance;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return () -> instance;
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }
}
