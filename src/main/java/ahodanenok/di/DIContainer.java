package ahodanenok.di;

import ahodanenok.di.exception.ScopeResolutionException;
import ahodanenok.di.exception.UnknownScopeException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.scope.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.*;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318) (https://docs.oracle.com/javaee/6/api/javax/interceptor/package-summary.html)
// todo: implement decorators
// todo: default values, which are injected when no others are found (@Default)
// todo: JSR-250
// todo: logging
// todo: events (like in cdi)
// todo: qualifiers @Any, @Default
// todo: stereotypes
// todo: injection points (could be injected)
// todo: alternatives

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer {

    private Map<ScopeIdentifier, Scope> scopes;
    private ScopeResolution scopeResolution;
    private QualifierResolution qualifierResolution;

    private Set<DependencyValue<?>> values;
    private DependencyValueLookup valueLookup;

    private DIContainer() {
        this.values = new HashSet<>();
        this.scopes = new HashMap<>();
    }

    public ScopeResolution scopeResolution() {
        return scopeResolution;
    }

    public QualifierResolution qualifierResolution() {
        return qualifierResolution;
    }

    private Scope lookupScope(ScopeIdentifier id) {
        Scope scope = scopes.get(id);
        if (scope != null) {
            return scope;
        } else {
            throw new UnknownScopeException(id);
        }
    }

    private <T> DependencyValue<T> lookupValue(DependencyIdentifier<T> id) {
        Set<DependencyValue<T>> result = valueLookup.execute(values, id);

        if (result.isEmpty()) {
            return null;
        }

        if (result.size() > 2) {
            throw new UnsatisfiedDependencyException(id, "multiple providers");
        }

        DependencyValue<T> value = result.iterator().next();
        // todo: cache looked up values
        return value;
    }

    public <T> Provider<? extends T> provider(Class<T> type) {
        return provider(DependencyIdentifier.of(type));
    }

    public <T> Provider<? extends T> provider(DependencyIdentifier<T> id) {
        DependencyValue<T> value = lookupValue(id);
        if (value == null) {
            return () -> null;
        }

        Scope scope = lookupScope(value.scope());
        return () -> scope.get(value.id(), value.provider());
    }

    public <T> T instance(Class<T> type) {
        return provider(DependencyIdentifier.of(type)).get();
    }

    public <T> T instance(DependencyIdentifier<T> id) {
        return provider(id).get();
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

    public static Builder builder() {
        DIContainer container = new DIContainer();
        return container.new Builder();
    }

    public class Builder {

        public Builder addValue(DependencyValue<?> value) {
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
            DIContainer.this.scopeResolution = scopeResolution;
            return this;
        }

        public Builder withQualifierResolution(QualifierResolution qualifierResolution) {
            DIContainer.this.qualifierResolution = qualifierResolution;
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

            if (container.scopeResolution == null) {
                container.scopeResolution = new AnnotatedScopeResolution();
            }

            if (container.qualifierResolution == null) {
                container.qualifierResolution = new AnnotatedQualifierResolution();
            }

            if (container.valueLookup == null) {
                container.valueLookup = new DependencyValueExactLookup();
            }

            for (DependencyValue<?> value : container.values) {
                value.bind(container);
            }

            for (DependencyValue<?> value : container.values) {
                if (value.isInitOnStartup()) {
                    if (value.scope().equals(ScopeIdentifier.of(Singleton.class))) {
                        instance(value.id());
                    } else {
                        throw new IllegalStateException("Eager initialization is only applicable to singleton values");
                    }
                }
            }

            return container;
        }
    }
}
