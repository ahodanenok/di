package ahodanenok.di;

import javax.inject.Provider;

public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private T instance;

    public DependencyInstanceValue(Class<T> type, T instance) {
        super(type);
        this.instance = instance;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return () -> instance;
    }
}
