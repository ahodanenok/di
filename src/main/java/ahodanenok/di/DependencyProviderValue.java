package ahodanenok.di;

import ahodanenok.di.annotation.DefaultScope;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;

import javax.inject.Provider;

public class DependencyProviderValue<T> extends AbstractDependencyValue<T> {

    private Provider<? extends T> provider;
    private ScopeIdentifier scope;
    private ScopeResolution scopeResolution;

    public DependencyProviderValue(Class<T> type, Provider<? extends T> provider) {
        super(type);
        this.provider = provider;
        this.scopeResolution = new AnnotatedScopeResolution();
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return provider;
    }

    @Override
    public ScopeIdentifier scope() {
        if (scope == null) {
            scope = scopeResolution.resolve(provider.getClass(), ScopeIdentifier.of(DefaultScope.class));
        }

        return scope;
    }
}
