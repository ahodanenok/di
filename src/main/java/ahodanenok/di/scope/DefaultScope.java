package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;

import javax.inject.Provider;

// todo: name clashes with @DefaultScope
public class DefaultScope implements Scope {

    private ScopeIdentifier id = ScopeIdentifier.of(ahodanenok.di.annotation.DefaultScope.class);

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(DependencyIdentifier<T> id, Provider<? extends T> provider) {
        return provider.get();
    }
}
