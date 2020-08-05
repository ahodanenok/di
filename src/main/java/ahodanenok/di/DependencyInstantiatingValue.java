package ahodanenok.di;

import ahodanenok.di.exception.DependencyInstantiatingException;
import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DependencyInstantiatingValue<T> extends AbstractDependencyValue<T> {

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
            Set<Annotation> qualifiers = container.qualifierResolution().resolve(instanceClass);
            id = DependencyIdentifier.of(type, qualifiers);
        }

        Supplier<Set<Annotation>> stereotypes = () -> container.stereotypeResolution().resolve(instanceClass);

        scope = container.scopeResolution().resolve(instanceClass, stereotypes, ScopeIdentifier.of(NotScoped.class));

        if (name == null) {
            setName(container.nameResolution().resolve(instanceClass, stereotypes));
        }

        if (initOnStartup == null && instanceClass.isAnnotationPresent(Eager.class)) {
            setInitOnStartup(true);
        }

        if (defaultValue == null && instanceClass.isAnnotationPresent(DefaultValue.class)) {
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
                if (targetConstructor == null) {
                    Constructor<? extends T> constructor = resolveConstructor();
                    targetConstructor = new InjectableConstructor<>(container, constructor);
//                    targetConstructor.setAroundConstructObserver();
                }

                T instance = targetConstructor.inject();
                if (instance != null) {
                    container.inject(instance);
                    // todo: post create
                }

                return instance;
        };
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }

    private Constructor<? extends T> resolveConstructor() {
        // todo: conform to spec
        // +Injectable constructors are annotated with @Inject and accept zero or more dependencies as arguments.
        // +@Inject can apply to at most one constructor per class.
        // @Inject is optional for public, no-argument constructors when no other constructors are present.
        // This enables injectors to invoke default constructors.

        // Injectable constructors are annotated with @Inject
        Set<Constructor<?>> constructors = Arrays.stream(instanceClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());

        // @Inject can apply to at most one constructor per class.
        if (constructors.size() > 1) {
            throw new DependencyInstantiatingException(id, instanceClass,
                    "multiple constructors are marked with @Inject," +
                    " to make this class available for dependency injection leave only such constructor," +
                    " constructors: " + constructors);
        }

        Constructor<? extends T> constructor = null;
        if (!constructors.isEmpty()) {
            @SuppressWarnings("unchecked") // constructor comes from instanceClass, which is T or its subclass
            Constructor<? extends T> c = (Constructor<? extends T>) constructors.iterator().next();
            constructor = c;
        } else {
            try {
                // @Inject is optional for public, no-argument constructors
                if (!Modifier.isStatic(instanceClass.getModifiers())
                        // todo: leave only support for member classes
                        && (instanceClass.isMemberClass() || instanceClass.isLocalClass() || instanceClass.isAnonymousClass())) {
                    constructor = instanceClass.getConstructor(instanceClass.getEnclosingClass());
                } else {
                    constructor = instanceClass.getConstructor();
                }
            } catch (NoSuchMethodException e) {
                throw new DependencyInstantiatingException(id, instanceClass, "default constructor is not found, mark appropriate constructor with @Inject" +
                        " to make this class available for dependency injection, constructors: " + Arrays.asList(instanceClass.getDeclaredConstructors()));
            }

            // @Inject is optional for public, no-argument constructors when no other constructors are present.
            if (instanceClass.getDeclaredConstructors().length > 1) {
                throw new DependencyInstantiatingException(id, instanceClass, "multiple constructors are found, mark appropriate constructor with @Inject" +
                        " to make this class available for dependency injection, constructors: " + Arrays.asList(instanceClass.getDeclaredConstructors()));
            }
        }


        if (constructor == null) {
            throw new DependencyInstantiatingException(id, instanceClass,
                    "no applicable constructor found, either leave only one constructor without parameters or mark some constructor with @Inject");
        }


        return constructor;
    }
}
