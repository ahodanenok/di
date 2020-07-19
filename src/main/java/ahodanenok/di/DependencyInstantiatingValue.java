package ahodanenok.di;

import ahodanenok.di.exception.DependencyInstantiatingException;
import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInstantiatingValue<T> implements DependencyValue<T> {

    // todo: check that class is really an instantiable class

    private DIContainer container;

    private DependencyIdentifier<T> id;
    private Class<T> type;

    private ScopeIdentifier scope;

    private Class<? extends T> instanceClass;
    private InjectableConstructor<? extends T> targetConstructor;

    public DependencyInstantiatingValue(Class<T> instanceClass) {
        this(instanceClass, instanceClass);
    }

    public DependencyInstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        if (!ReflectionAssistant.isInstantiable(instanceClass)) {
            throw new IllegalArgumentException("Can't create an instance of a class: " +  instanceClass.getName());
        }

        this.type = type;
        this.instanceClass = instanceClass;
    }

    /**
     *
     * Note that qualifiers on instanceClass will be ignore,
     * as it is assumed that all necessary qualifiers present in id
     *
     * @param id
     * @param instanceClass
     */
    public DependencyInstantiatingValue(DependencyIdentifier<T> id, Class<? extends T> instanceClass) {
        this(id.type(), instanceClass);
        this.id = id;
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;

        if (id == null) {
            Annotation qualifier = container.qualifierResolution().resolve(instanceClass);
            id = DependencyIdentifier.of(type, qualifier);
        }

        scope = container.scopeResolution().resolve(instanceClass, ScopeIdentifier.of(NotScoped.class));
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public Provider<? extends T> provider() {
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

        if (constructors.size() > 1) {
            throw new DependencyInstantiatingException(id, "multiple injection points: " + constructors);
        }

        Constructor<? extends T> constructor = null;
        if (!constructors.isEmpty()) {
            @SuppressWarnings("unchecked") // constructor comes from instanceClass, which is T or its subclass
            Constructor<? extends T> c = (Constructor<? extends T>) constructors.iterator().next();
            constructor = c;
        } else {
            // todo: check that there are no other constructors except default, in that case some constructor must be annotated with @Inject
            try {
                constructor = instanceClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new DependencyInstantiatingException(id, "no default constructor");
            }
        }

        if (constructor == null) {
            throw new DependencyInstantiatingException(id, "no applicable constructor for injection");
        }


        return constructor;
    }
}
