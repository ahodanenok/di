package ahodanenok.di;

import javax.inject.Provider;

public class DependencyProviderValue<T> extends AbstractDependencyValue<T> {

    private Provider<? extends T> provider;

    public DependencyProviderValue(Class<T> type, Provider<? extends T> provider) {
        super(type);
        this.provider = provider;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return provider;
    }
}
