package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;

import javax.inject.Provider;

public class DefaultScope implements Scope {

    private ScopeIdentifier id = ScopeIdentifier.of(NotScoped.class);

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(DependencyIdentifier<T> id, Provider<? extends T> provider) {
        return provider.get();
    }
}
