package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;

public class DependencyProviderValue<T> extends AbstractDependencyValue<T> {

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
    public void bind(DIContainer container) {
        providerValue.bind(container);
        id = DependencyIdentifier.of(type, providerValue.id().qualifiers());
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public ScopeIdentifier scope() {
        return providerValue.scope();
    }

    @Override
    public Provider<? extends T> provider() {
        return providerValue.provider().get();
    }

    @Override
    public boolean isDefault() {
        if (defaultValue == null) {
            return providerValue.isDefault();
        }

        return super.isDefault();
    }

    @Override
    public boolean isInitOnStartup() {
        if (initOnStartup == null) {
            return providerValue.isInitOnStartup();
        }

        return super.isInitOnStartup();
    }
}
