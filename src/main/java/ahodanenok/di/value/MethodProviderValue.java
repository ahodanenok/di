package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.value.metadata.MethodMetadata;

import javax.inject.Provider;
import java.lang.reflect.Method;

// todo: @Disposes method for created instance
public class MethodProviderValue<T> extends AbstractValue<T> {

    private DependencyIdentifier<?> methodInstanceId;
    private Method method;

    public MethodProviderValue(Class<T> type, Method method) {
        this(type, null, method);
    }

    // todo: don't need explicit id for declaring class, id can be created from it
    public MethodProviderValue(Class<T> type, DependencyIdentifier<?> methodInstanceId, Method method) {
        super(type, new MethodMetadata(type, method));

        // todo: check if method is not static and no instance given
        this.methodInstanceId = methodInstanceId;

        // todo: check method return type is compatible with type
        this.method = method;
    }

    @Override
    public Provider<? extends T> provider() {
        // todo: if like in ahodanenok.di.value.DependencyFieldProviderValue.provider
        return () -> {
            // todo: allow specifying provider method in supeclass or interface, concrete implementation will provide it
            // todo: check for conflicts on annotations on provider method from interface and its implementation
            // todo: how to register instance of declaring class in container if method is not static

            Object instance = methodInstanceId != null ? container.instance(methodInstanceId) : null;
            // todo: if method is not static instance is required, throw unsatisfied dependency if null
            // todo: suppress warnings when type is checked in constructor
            return (T) new InjectableMethod(container, method).inject(instance);
        };
    }
}