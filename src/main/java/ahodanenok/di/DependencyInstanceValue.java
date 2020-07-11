package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;

public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private T instance;
    private ScopeIdentifier scope;

    public <V extends T> DependencyInstanceValue(Class<T> type, V instance) {
        super(type);
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
