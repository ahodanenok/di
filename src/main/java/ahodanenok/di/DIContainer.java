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

    private Set<Value<?>> values;
    private ValueLookup valueLookup;

    private Set<Value<?>> interceptors;
    private InterceptorLookup interceptorLookup;

    private Map<Member, InterceptorChain> resolvedInterceptorChains;

    private DIContainer() {
        this.values = new HashSet<>();
        this.scopes = new HashMap<>();
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
        Set<Value<?>> result = values.stream().filter(v -> name.equals(v.metadata().getName())).collect(Collectors.toSet());
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
                                "values are " + result.stream().map(Value::type).collect(Collectors.toList()));
            }

            result = withoutDefaults;
        }

        Value<T> value = result.iterator().next();
        return provider(value);
    }

    private <T> Provider<? extends T> provider(Value<T> value) {
        Objects.requireNonNull(value, "value is null")    ;
        Scope scope = lookupScope(value.metadata().getScope());
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
            new InjectableField(this, f).inject(instance);
        });

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.
        ReflectionAssistant.methods(instance.getClass()).filter(m -> m.isAnnotationPresent(Inject.class)).forEach(m -> {
            // todo: cache
            // todo: which methods should be skipped (i.e inherited)
            new InjectableMethod(this, m).inject(instance);
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
            List<Value<?>> classInterceptors = interceptorLookup.lookup(this, aroundConstruct.getConstructor().getDeclaringClass(), interceptors);
            List<Method> methods = new ArrayList<>();
            for (Value<?> interceptor : classInterceptors) {
                Method aroundConstructMethod = instance(InterceptorMetadataResolution.class)
                        .resolveAroundConstruct(interceptor.metadata().valueType());
                if (aroundConstructMethod != null) {
                    methods.add(aroundConstructMethod);
                }
            }

//            Set<Annotation> bindings = context.getInterceptorBindingsResolution().resolve(aroundConstruct.getConstructor());
//            List<Method> methods = interceptors.aroundConstructInterceptorMethods(aroundConstruct.getConstructor());
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

        private List<Scope> scopes;
        private List<Value<?>> values;

        public Builder addValue(Value<?> value) {
            if (values == null) {
                values = new ArrayList<>();
            }

            values.add(value);
            return this;
        }

        // todo: how to discover provider methods
//        public Builder scan(Class<?> clazz) {
//
//        }

        public Builder addScope(Scope scope) {
            if (scopes == null) {
                scopes = new ArrayList<>();
            }

            scopes.add(scope);
            return this;
        }

        /**
         * Provide a custom implementation of dependencies lookup.
         * Default implementation is {@link ValueExactLookup}
         */
        public Builder withValuesLookup(ValueLookup valueLookup) {
            DIContainer.this.valueLookup = valueLookup;
            return this;
        }

        /**
         * Provide a custom implementation of scope resolution.
         * Default implementation is {@link AnnotatedScopeResolution}
         */
        public Builder withScopeResolution(ScopeResolution scopeResolution) {
            //this.scopeResolution = scopeResolution; // todo
            return this;
        }

        public Builder withQualifierResolution(QualifierResolution qualifierResolution) {
            //this.qualifierResolution = qualifierResolution; // todo
            return this;
        }

        public Builder withNameResolution(NameResolution nameResolution) {
            //this.nameResolution = nameResolution; todo
            return this;
        }

        public Builder withStereotypeResolution(StereotypeResolution stereotypeResolution) {
            //this.stereotypeResolution = stereotypeResolution; todo
            return this;
        }

        public DIContainer build() {
            DIContainer container = DIContainer.this;

            // custom scopes with the same identifier override built-in scopes
            Set<Scope> scopes = new HashSet<>();
            scopes.add(new DefaultScope());
            scopes.add(new SingletonScope());
            if (this.scopes != null) {
                scopes.addAll(this.scopes);
            }
            for (Scope scope : scopes) {
                container.scopes.putIfAbsent(scope.id(), scope);
            }

            if (container.valueLookup == null) {
                container.valueLookup = new ValueExactLookup();
            }

            if (container.interceptorLookup == null) {
                container.interceptorLookup = new DefaultInterceptorLookup();
            }

            InstanceValue<ScopeResolution> scopeResolution =
                    new InstanceValue<>(ScopeResolution.class, new AnnotatedScopeResolution());
            scopeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            scopeResolution.metadata().setDefault(true);
            container.values.add(scopeResolution);

            InstanceValue<QualifierResolution> qualifierResolution =
                    new InstanceValue<>(QualifierResolution.class, new AnnotatedQualifierResolution());
            qualifierResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            qualifierResolution.metadata().setDefault(true);
            container.values.add(qualifierResolution);

            InstanceValue<NameResolution> nameResolution =
                    new InstanceValue<>(NameResolution.class, new AnnotatedNameResolution());
            nameResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            nameResolution.metadata().setDefault(true);
            container.values.add(nameResolution);

            InstanceValue<StereotypeResolution> stereotypeResolution =
                    new InstanceValue<>(StereotypeResolution.class, new AnnotatedStereotypeResolution());
            stereotypeResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            stereotypeResolution.metadata().setDefault(true);
            container.values.add(stereotypeResolution);

            InstanceValue<InterceptorMetadataResolution> interceptorMetadataResolution =
                    new InstanceValue<>(InterceptorMetadataResolution.class, new AnnotatedInterceptorMetadataResolution());
            interceptorMetadataResolution.metadata().setScope(ScopeIdentifier.SINGLETON);
            interceptorMetadataResolution.metadata().setDefault(true);
            container.values.add(interceptorMetadataResolution);

            container.values.add(new InstanceValue<>(new Events() {
                @Override
                public void fire(Event event) {
                    container.handleEvent(event);
                }
            }));

            if (values != null) {
                for (Value<?> value : values) {
                    value.bind(container);
                }

                container.values.addAll(values);
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

            return container;
        }
    }
}
