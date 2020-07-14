package ahodanenok.di;

import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DependencyMethodProviderValue<T> implements DependencyValue<T> {

    private DependencyIdentifier<T> id;
    private Class<T> type;

    private DependencyIdentifier<?> methodInstanceId;
    private Method method;

    private ScopeIdentifier scope;
    private ScopeResolution scopeResolution;
    private QualifierResolution qualifierResolution;

    public DependencyMethodProviderValue(Class<T> type, Method method) {
        this(type, null, method);
    }

    public DependencyMethodProviderValue(Class<T> type, DependencyIdentifier<?> methodInstanceId, Method method) {
        this.type = type;

        // todo: check if method is not static and no instance given
        this.methodInstanceId = methodInstanceId;

        // todo: check method return type is compatible with type
        this.method = method;

        // todo: get ScopeResolution from container
        this.scopeResolution = new AnnotatedScopeResolution();
        // todo : get QualifierResolution from container
        this.qualifierResolution = new AnnotatedQualifierResolution();
    }

    @Override
    public DependencyIdentifier<T> id() {
        if (id == null) {
            Annotation qualifier = qualifierResolution.resolve(method);
            id = DependencyIdentifier.of(type, qualifier);
        }

        return id;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return () -> {
            Object instance = methodInstanceId != null ? container.instance(methodInstanceId) : null;
            // todo: suppress warnings when type is checked in constructor
            return (T) new InjectableMethod(container, method).inject(instance);
        };
    }

    @Override
    public ScopeIdentifier scope() {
        if (scope == null) {
            // todo: maybe use scope of method owner class as a default
            scope = scopeResolution.resolve(method, ScopeIdentifier.of(NotScoped.class));
        }

        return scope;
    }
}
