package ahodanenok.di;

import java.util.function.Supplier;

public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private T instance;

    public DependencyInstanceValue(Class<T> type, T instance) {
        super(type);
        this.instance = instance;
    }

    @Override
    public Supplier<? extends T> supplier(DIContainer container) {
        return () -> instance;
    }
}
