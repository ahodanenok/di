package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.event.AroundProvisionEvent;
import ahodanenok.di.event.Events;
import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.inject.Provider;
import java.lang.reflect.Method;

// todo: @Disposes method for created instance
public class MethodProviderValue<T> extends AbstractValue<T> {

    private ValueSpecifier<?> methodInstanceId;
    private Method method;

    private Events events;

    public MethodProviderValue(Class<T> type, Method method) {
        this(type, null, method);
    }

    // todo: pass metadata
    // todo: don't need explicit id for declaring class, id can be created from it
    public MethodProviderValue(Class<T> type, ValueSpecifier<?> methodInstanceId, Method method) {
        super(type);

        // todo: check if method is not static and no instance given
        this.methodInstanceId = methodInstanceId;

        // todo: check method return type is compatible with type
        this.method = method;
    }

    @Override
    public Class<? extends T> realType() {
        return (Class<? extends T>) method.getReturnType();
    }

    @Override
    public void bind(DIContainer container) {
        super.bind(container);
        this.events = container.instance(Events.class);
    }

    @Override
    protected MutableValueMetadata resolveMetadata() {
        return container.instance(ValueMetadataResolution.class).resolve(method);
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
            InjectableMethod injectableMethod = new InjectableMethod(container, method);
            injectableMethod.setOnProvision(ai -> events.fire(new AroundProvisionEvent(this, ai)));
            return (T) injectableMethod.inject(instance);
        };
    }
}
