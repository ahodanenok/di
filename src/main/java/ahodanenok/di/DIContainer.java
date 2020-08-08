package ahodanenok.di;

import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.Event;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.UnknownScopeException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.*;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.*;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import ahodanenok.di.value.InstanceValue;
import ahodanenok.di.value.Value;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318) (https://docs.oracle.com/javaee/6/api/javax/interceptor/package-summary.html)
// todo: implement decorators
// todo: JSR-250
// todo: logging
// todo: events (like in cdi)
// todo: injection points (could be injected)
// todo: rethink exceptions' names
// todo: profiles (set of values enabled by name)
// todo: when null if valid as dependency value, when to throw unsatisfied dependency
// todo: default value from stereotype
// todo: profile from stereotype
// todo: documentation
// todo: generics
// todo: yaml config

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer {

    private Map<ScopeIdentifier, Scope> scopes;
    private DIContainerContext context;

    private Set<Value<?>> values;
    private DependencyValueLookup valueLookup;

    private InterceptorRegistry interceptors;
    private Map<Member, InterceptorChain> resolvedInterceptorChains;

    private DIContainer() {
        this.values = new HashSet<>();
        this.scopes = new HashMap<>();
        this.context = new DIContainerContext(this);
    }

    DIContainerContext getContext() {
        return context;
    }

    private Scope lookupScope(ScopeIdentifier id) {
        Scope scope = scopes.get(id);
        if (scope != null) {
            return scope;
        } else {
            throw new UnknownScopeException(id);
        }
    }

    public Provider<?> provider(String name) {
        // todo: names
        Set<Value<?>> result = Collections.emptySet();//values.stream().filter(v -> name.equals(v.getName())).collect(Collectors.toSet());
        if (result.isEmpty()) {
            return null;
        }

        if (result.size() > 1) {
            Set<Value<?>> withoutDefaults = result.stream().filter(v -> !v.metadata().isDefault()).collect(Collectors.toSet());
            if (withoutDefaults.size() > 1) {
                //throw new UnsatisfiedDependencyException(id, "multiple providers");
                throw new RuntimeException(); // todo: errors
            }

            if (result.size() - withoutDefaults.size() > 1) {
//                throw new IllegalStateException(
//                        "There are multiple values marked as default for " + id + ", " +
//                                "values are " + values.stream().map(DependencyValue::id).collect(Collectors.toList()));
                throw new IllegalStateException(); // todo: errors
            }

            result = withoutDefaults;
        }

        Value<?> value = result.iterator().next();
        return provider(value);
    }

    public <T> Provider<? extends T> provider(Class<T> type) {
        return provider(DependencyIdentifier.of(type));
    }

    public <T> Provider<? extends T> provider(DependencyIdentifier<T> id) {
        Set<Value<T>> result = valueLookup.execute(values, id);
        if (result.isEmpty()) {
            return null;
        }

        if (result.size() > 1) {
            Set<Value<T>> withoutDefaults = result.stream().filter(v -> !v.metadata().isDefault()).collect(Collectors.toSet());
            if (withoutDefaults.size() > 1) {
                throw new UnsatisfiedDependencyException(id, "multiple providers"); // todo: errors
            }

            if (result.size() - withoutDefaults.size() > 1) {
                // todo: errors
                throw new IllegalStateException(
                        "There are multiple values marked as default for " + id + ", " +
                                "values are " + result.stream().map(Value::id).collect(Collectors.toList()));
            }

            result = withoutDefaults;
        }

        Value<T> value = result.iterator().next();
        return provider(value);
    }

    private <T> Provider<? extends T> provider(Value<T> value) {
        Objects.requireNonNull(value, "value is null")    ;
        Scope scope = lookupScope(value.metadata().scope());
        return () -> scope.get(value);
    }

    public <T> T instance(Class<T> type) {
        Provider<? extends T> p = provider(DependencyIdentifier.of(type));
        if (p == null) {
            return null;
        }

        return p.get();
    }

    public <T> T instance(DependencyIdentifier<T> id) {
        Provider<? extends T> p = provider(id);
        if (p == null) {
            return null;
        }

        return p.get();
    }

    public void inject(Object instance) {
        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }

        // todo: Fields and methods in superclasses are injected before those in subclasses.
        // todo: check circular references

        ReflectionAssistant.fields(instance.getClass()).filter(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
            // todo: cache
            // todo: which fields should be skipped (i.e inherited)
            new InjectableField(context, f).inject(instance);
        });

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.
        ReflectionAssistant.methods(instance.getClass()).filter(m -> m.isAnnotationPresent(Inject.class)).forEach(m -> {
            // todo: cache
            // todo: which methods should be skipped (i.e inherited)
            new InjectableMethod(context, m).inject(instance);
        });
    }

    private void handleEvent(Event event) {
        // todo: simple handling for now just to implement aroundConstruct
        if (event instanceof AroundConstructEvent) {
            interceptAroundConstruct(((AroundConstructEvent<?>) event).getAroundConstruct());
        }
    }

    private void interceptAroundConstruct(AroundConstruct<?> aroundConstruct) {
        if (interceptors == null) {
            try {
                aroundConstruct.proceed();
            } catch (ReflectiveOperationException e) {
                // todo: error
                throw new RuntimeException(e);
            }

            return;
        }

        if (resolvedInterceptorChains == null) {
            resolvedInterceptorChains = new HashMap<>();
        }

        InterceptorChain chain = resolvedInterceptorChains.get(aroundConstruct.getConstructor());
        if (chain == null) {
//            Set<Annotation> bindings = context.getInterceptorBindingsResolution().resolve(aroundConstruct.getConstructor());
            List<Method> methods = interceptors.aroundConstructInterceptorMethods(aroundConstruct.getConstructor());
            chain = new InterceptorChain(aroundConstruct, methods);
            resolvedInterceptorChains.put(aroundConstruct.getConstructor(), chain);
        }

        try {
            chain.proceed();
        } catch (Exception e) {
            // todo: error
            throw new RuntimeException(e);
        }
    }

    public static Builder builder() {
        DIContainer container = new DIContainer();
        return container.new Builder();
    }

    public class Builder {

        public Builder addValue(Value<?> value) {
            DIContainer.this.values.add(value);
            return this;
        }

        // todo: how to discover provider methods
//        public Builder scan(Class<?> clazz) {
//
//        }

        public Builder addScope(Scope scope) {
            DIContainer.this.scopes.put(scope.id(), scope);
            return this;
        }

        /**
         * Provide a custom implementation of dependencies lookup.
         * Default implementation is {@link DependencyValueExactLookup}
         */
        public Builder withValuesLookup(DependencyValueLookup valueLookup) {
            DIContainer.this.valueLookup = valueLookup;
            return this;
        }

        /**
         * Provide a custom implementation of scope resolution.
         * Default implementation is {@link AnnotatedScopeResolution}
         */
        public Builder withScopeResolution(ScopeResolution scopeResolution) {
            DIContainer.this.context.scopeResolution = scopeResolution;
            return this;
        }

        public Builder withQualifierResolution(QualifierResolution qualifierResolution) {
            DIContainer.this.context.qualifierResolution = qualifierResolution;
            return this;
        }

        public Builder withNameResolution(NameResolution nameResolution) {
            DIContainer.this.context.nameResolution = nameResolution;
            return this;
        }

        public Builder withStereotypeResolution(StereotypeResolution stereotypeResolution) {
            DIContainer.this.context.stereotypeResolution = stereotypeResolution;
            return this;
        }

        public DIContainer build() {
            DIContainer container = DIContainer.this;

            // custom scopes with the same identifier override built-in scopes
            Set<Scope> scopes = new HashSet<>();
            scopes.add(new DefaultScope());
            scopes.add(new SingletonScope());
            // todo: load custom scopes
            for (Scope scope : scopes) {
                container.scopes.putIfAbsent(scope.id(), scope);
            }

            if (container.context.scopeResolution == null) {
                container.context.scopeResolution = new AnnotatedScopeResolution();
            }

            if (container.context.qualifierResolution == null) {
                container.context.qualifierResolution = new AnnotatedQualifierResolution();
            }

            if (container.context.nameResolution == null) {
                container.context.nameResolution = new AnnotatedNameResolution();
            }

            if (container.context.stereotypeResolution == null) {
                container.context.stereotypeResolution = new AnnotatedStereotypeResolution();
            }

            if (container.context.interceptorMetadataResolution == null) {
                container.context.interceptorMetadataResolution = new AnnotatedInterceptorMetadataResolution();
            }

            container.context.aroundConstructObserver = DIContainer.this::interceptAroundConstruct;

            if (container.valueLookup == null) {
                container.valueLookup = new DependencyValueExactLookup();
            }

            Events events = new Events() {
                @Override
                public void fire(Event event) {
                    container.handleEvent(event);
                }
            };

            container.values.add(new InstanceValue<>(events));
            for (Value<?> value : container.values) {
                value.bind(context);
            }

            // todo: eager init
//            for (Value<?> value : container.values) {
//                if (value.isInitOnStartup()) {
//                    if (value.scope().equals(ScopeIdentifier.of(Singleton.class))) {
//                        instance(value.id());
//                    } else {
//                        throw new IllegalStateException("Eager initialization is only applicable to singleton values");
//                    }
//                }
//            }

//            if (interceptorClasses != null) {
//                container.interceptors = new InterceptorRegistry(container, container.context.interceptorMetadataResolution);
//                container.interceptors.setInterceptorClasses(interceptorClasses);
//            }

            return container;
        }
    }
}
