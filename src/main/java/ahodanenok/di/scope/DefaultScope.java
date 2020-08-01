package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;
import ahodanenok.di.DependencyValue;

import javax.inject.Provider;

/**
 * @see NotScoped
 */
public class DefaultScope implements Scope {

    private ScopeIdentifier id = ScopeIdentifier.of(NotScoped.class);

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(DependencyValue<T> value) {
        return value.provider().get();
    }
}
