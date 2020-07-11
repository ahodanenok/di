package ahodanenok.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectableMethod implements Injectable<Object> {

    private DIContainer container;
    private Method method;

    public InjectableMethod(DIContainer container, Method method) {
        this.container = container;
        this.method = method;
    }

    @Override
    public Object inject(Object instance) {

        // todo: common code here and in InjectableConstructor

        // todo: conform to spec
        // Injectable methods:
        // are annotated with @Inject.
        // are not abstract.
        // do not declare type parameters of their own.
        // may return a result
        // may have any otherwise valid name.
        // accept zero or more dependencies as arguments.

        int i = 0;
        Object[] args = new Object[method.getParameterCount()];
        for (Class<?> type : method.getParameterTypes()) {
            // todo: qualifier
            args[i++] = container.instance(DependencyIdentifier.of(type));
        }

        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {

            // todo: errors
            throw new RuntimeException(e);
        }

        return instance;
    }
}
