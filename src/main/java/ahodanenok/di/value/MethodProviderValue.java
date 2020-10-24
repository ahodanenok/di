package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.event.AroundProvisionEvent;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.qualifier.QualifierResolution;
import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.inject.Provider;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// todo: @Disposes method for created instance
public final class MethodProviderValue<T> extends AbstractValue<T> {

    private ValueSpecifier<?> methodInstanceId;
    private final Method method;

    private Events events;

    public MethodProviderValue(Class<T> type, Method method) {
        this(type, null, method);
    }

    public MethodProviderValue(Class<T> type, ValueSpecifier<?> methodInstanceId, Method method) {
        super(type);

        if (Modifier.isStatic(method.getModifiers()) && methodInstanceId != null) {
            throw new IllegalArgumentException("Instance specifier for static provider method is not needed");
        }

        if (!type.isAssignableFrom(method.getReturnType())) {
            throw new IllegalArgumentException(String.format(
                    "Method's return type '%s' is not compatible with declared value type '%s'",
                    method.getReturnType(), type));
        }

        this.methodInstanceId = methodInstanceId;
        this.method = method;
    }

    @Override
    @SuppressWarnings("unchecked") // type is checked in constructor
    public Class<? extends T> realType() {
        return (Class<? extends T>) method.getReturnType();
    }

    @Override
    public void bind(DIContainer container) {
        super.bind(container);
        this.events = container.instance(Events.class);

        if (!Modifier.isStatic(method.getModifiers()) && methodInstanceId == null) {
            methodInstanceId = ValueSpecifier.of(
                    method.getDeclaringClass(),
                    container.instance(QualifierResolution.class).resolve(method.getDeclaringClass()));
        }
    }

    @Override
    protected MutableValueMetadata resolveMetadata() {
        return container.instance(ValueMetadataResolution.class).resolve(method);
    }

    @Override
    @SuppressWarnings("unchecked") // type is checked in constructor
    public Provider<? extends T> provider() {
        return () -> {
            // todo: allow specifying provider method in supeclass or interface, concrete implementation will provide it
            // todo: check for conflicts on annotations on provider method from interface and its implementation

            InjectableMethod injectableMethod = new InjectableMethod(container, method);
            injectableMethod.setOnProvision(ai -> events.fire(new AroundProvisionEvent(this, ai)));
            if (Modifier.isStatic(method.getModifiers())) {
                return (T) injectableMethod.inject(null);
            } else {
                Object instance = container.instance(methodInstanceId);
                if (instance == null) {
                    throw new UnsatisfiedDependencyException(
                            String.format("Couldn't invoke method '%s' because container doesn't have " +
                                    "an instance of its declaring class '%s'", method, methodInstanceId));
                }

                return (T) injectableMethod.inject(instance);
            }
        };
    }
}
