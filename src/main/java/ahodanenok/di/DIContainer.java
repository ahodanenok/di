package ahodanenok.di;

import ahodanenok.di.exception.ScopeResolutionException;
import ahodanenok.di.exception.UnknownScopeException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.scope.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318)
// todo: logging

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer {

    private Map<ScopeIdentifier, Scope> scopes;
    private ScopeResolution scopeResolution;
    private QualifierResolution qualifierResolution;

    // todo: is it necessary to keep track of injection locations
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
            new InjectableField(this, f).inject(instance);
        });

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.
        ReflectionAssistant.methods(instance.getClass()).filter(m -> m.isAnnotationPresent(Inject.class)).forEach(m -> {
            // todo: cache
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

        public Builder addScope(Scope scope) {
            DIContainer.this.scopes.put(scope.id(), scope);
            return this;
        }

        public Builder withValuesLookup(DependencyValueLookup valueLookup) {
            DIContainer.this.valueLookup = valueLookup;
            return this;
        }

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

            return container;
        }
    }
}
