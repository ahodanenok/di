package ahodanenok.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainer container;
    private Constructor<? extends T> constructor;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        this.container = container;
        this.constructor = constructor;
    }

    @Override
    public T inject(T instance) {
        try {

            // todo: handle parameter annotations
            // todo: handle generic types
            // todo: common code here and in InjectableMethod

            int i = 0;
            Object[] args = new Object[constructor.getParameterCount()];
            for (Class<?> type : constructor.getParameterTypes()) {
                // todo: qualifier
                args[i++] = container.instance(DependencyIdentifier.of(type));
            }

            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // todo: errors
            throw new RuntimeException(e);
        }
    }
}
