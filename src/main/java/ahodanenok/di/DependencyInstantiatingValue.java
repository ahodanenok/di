package ahodanenok.di;

import ahodanenok.di.annotation.DefaultScope;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInstantiatingValue<T> implements DependencyValue<T> {

    // todo: check that class is really an instantiable class

    private DependencyIdentifier<T> id;
    private Class<T> type;
    private Class<? extends T> instanceClass;
    private ScopeIdentifier scope;
    private InjectableConstructor<? extends T> targetConstructor;
    private ScopeResolution scopeResolution;
    private QualifierResolution qualifierResolution;

    public DependencyInstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        this.type = type;
        this.instanceClass = instanceClass;
        // todo: get ScopeResolution from container
        this.scopeResolution = new AnnotatedScopeResolution();
        // todo : get QualifierResolution from container
        this.qualifierResolution = new AnnotatedQualifierResolution();
    }

    @Override
    public DependencyIdentifier<T> id() {
        if (id == null) {
            Annotation qualifier = qualifierResolution.resolve(instanceClass);
            id = DependencyIdentifier.of(type, qualifier);
        }

        return id;
    }

    @Override
    public Provider<? extends T> provider(DIContainer container) {
        return () -> {
                if (targetConstructor == null) {
                    Constructor<? extends T> constructor = resolveConstructor();
                    targetConstructor = new InjectableConstructor<>(container, constructor);
                }

                T instance = targetConstructor.inject();
                container.inject(instance);
                return instance;
        };
    }

    @Override
    public ScopeIdentifier scope() {
        if (scope == null) {
            scope = scopeResolution.resolve(instanceClass, ScopeIdentifier.of(DefaultScope.class));
        }

        return scope;
    }

    private Constructor<? extends T> resolveConstructor() {
        // todo: conform to spec
        // Injectable constructors are annotated with @Inject and accept zero or more dependencies as arguments.
        // @Inject can apply to at most one constructor per class.
        // @Inject is optional for public, no-argument constructors when no other constructors are present.
        // This enables injectors to invoke default constructors.

        Set<Constructor<?>> constructors = Arrays.stream(instanceClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());

        // todo: errors


        if (constructors.size() > 1) {
            throw new RuntimeException("multiple injection points");
        }

        Constructor<?> constructor = null;
        if (!constructors.isEmpty()) {
            constructor = constructors.iterator().next();
        } else {
            // todo: check that there are no other constructors except default, in that case some constructor must be annotated with @Inject
            try {
                constructor = instanceClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("no constructor");
            }
        }

        if (constructor == null) {
            throw new RuntimeException("no constructor");
        }

        return (Constructor<? extends T>) constructor;
    }
}
