package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;

public class DependencyProviderValue<T> implements DependencyValue<T> {

    private DependencyIdentifier<T> id;
    private Class<T> type;
    private DependencyValue<? extends Provider<? extends T>> providerValue;

    public <V extends Provider<? extends T>> DependencyProviderValue(Class<T> type, Class<V> providerClass) {
        this.type = type;
        this.providerValue = new DependencyInstantiatingValue<>(providerClass);
    }

    public <V extends Provider<? extends T>> DependencyProviderValue(Class<T> type, V provider) {
        this.type = type;
        this.providerValue = new DependencyInstanceValue<>(provider);
    }

    @Override
    public DependencyIdentifier<T> id() {
        if (id == null) {
            id = DependencyIdentifier.of(type, providerValue.id().qualifier());
        }

        return id;
    }

    @Override
    public ScopeIdentifier scope() {
        return providerValue.scope();
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return providerValue.provider(container).get();
    }
}
