package ahodanenok.di;

import java.util.function.Supplier;

public class DependencyInstantiatingValue<T> extends AbstractDependencyValue<T> {

    private Class<? extends T> instanceClass;

    public DependencyInstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        super(type);
        this.instanceClass = instanceClass;
    }

    @Override
    public Supplier<? extends T> supplier(DIContainer container) {
        return () -> {
            try {
                return instanceClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // todo: errors
                throw new RuntimeException(e);
            }
        };
    }
}
