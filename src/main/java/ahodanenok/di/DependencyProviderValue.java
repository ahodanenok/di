package ahodanenok.di;

import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;

import javax.inject.Provider;

public class DependencyProviderValue<T> extends AbstractDependencyValue<T> {

    private Provider<? extends T> provider;
    private ScopeIdentifier scope;
    private ScopeResolution scopeResolution;

    public DependencyProviderValue(DependencyIdentifier<T> id, Provider<? extends T> provider) {
        super(id);
        this.provider = provider;
        // todo: get ScopeResolution from container
        this.scopeResolution = new AnnotatedScopeResolution();
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return provider;
    }

    @Override
    public ScopeIdentifier scope() {
        if (scope == null) {
            scope = scopeResolution.resolve(provider.getClass(), ScopeIdentifier.of(NotScoped.class));
        }

        return scope;
    }
}
