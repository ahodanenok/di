package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;

public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private T instance;

    public <V extends T> DependencyInstanceValue(Class<T> type, V instance) {
        super(type);
        this.instance = instance;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return () -> instance;
    }

    @Override
    public ScopeIdentifier scope() {
        return new ScopeIdentifier(Singleton.class);
    }
}
