package ahodanenok.di;

import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

// todo: @Disposes method for created instance
public class DependencyMethodProviderValue<T> extends AbstractDependencyValue<T> {

    private DIContainer container;

    private DependencyIdentifier<T> id;
    private Class<T> type;

    private ScopeIdentifier scope;

    private DependencyIdentifier<?> methodInstanceId;
    private Method method;

    public DependencyMethodProviderValue(Class<T> type, Method method) {
        this(type, null, method);
    }

    // todo: don't need explicit id for declaring class, id can be created from it
    public DependencyMethodProviderValue(Class<T> type, DependencyIdentifier<?> methodInstanceId, Method method) {
        this.type = type;

        // todo: check if method is not static and no instance given
        this.methodInstanceId = methodInstanceId;

        // todo: check method return type is compatible with type
        this.method = method;
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;

        Set<Annotation> qualifiers = container.qualifierResolution().resolve(method);
        id = DependencyIdentifier.of(type, qualifiers);

        // todo: maybe use scope of method owner class as a default
        scope = container.scopeResolution().resolve(method, ScopeIdentifier.of(NotScoped.class));

        if (method.isAnnotationPresent(DefaultValue.class)) {
            setDefault(true);
        }
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public Provider<? extends T> provider() {
        return () -> {
            // todo: allow specifying provider method in supeclass or interface, concrete implementation will provide it
            // todo: check for conflicts on annotations on provider method from interface and its implementation
            // todo: how to register instance of declaring class in container if method is not static

            Object instance = methodInstanceId != null ? container.instance(methodInstanceId) : null;
            // todo: suppress warnings when type is checked in constructor
            return (T) new InjectableMethod(container, method).inject(instance);
        };
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }
}
