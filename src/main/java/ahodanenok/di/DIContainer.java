package ahodanenok.di;

import ahodanenok.di.scope.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement interceptors (https://jcp.org/en/jsr/detail?id=318)
// todo: logging

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
public final class DIContainer {

    private Map<String, Scope> scopes;
    private ReflectionAssistant reflectionAssistant;
    private ScopeResolution scopeResolution;
    private QualifierResolution qualifierResolution;

    // todo: is it necessary to keep track of injection locations
    private DependencyValueLookup dependencies;

    public DIContainer(DependencyValue<?>... values) {
        this.reflectionAssistant = new ReflectionAssistant();
        // todo: support custom scope resolution
        this.scopeResolution = new AnnotatedScopeResolution();
        // todo: support custom qualifier resolution
        this.qualifierResolution = new AnnotatedQualifierResolution();
        this.dependencies = new DependencyValueExactLookup(Arrays.stream(values).collect(Collectors.toSet()));
        this.scopes = new HashMap<>();

        // custom scopes with the same identifier override built-in scopes
        Set<Scope> scopes = new HashSet<>();
        scopes.add(new DefaultScope());
        scopes.add(new SingletonScope());
        // todo: load custom scopes

        for (Scope scope : scopes) {
            // todo: maybe use id as key?
            this.scopes.put(scope.id().get(), scope);
        }

        for (DependencyValue<?> value : values) {
            value.bind(this);
        }
    }

    public ScopeResolution scopeResolution() {
        return scopeResolution;
    }

    public QualifierResolution qualifierResolution() {
        return qualifierResolution;
    }

    private Scope lookupScope(String name) {
        Scope scope = scopes.get(name);
        if (scope != null) {
            return scope;
        } else {
            // todo: error
            throw new RuntimeException("no scope");
        }
    }

    public <T> Provider<? extends T> provider(Class<T> type) {
        return provider(DependencyIdentifier.of(type));
    }

    public <T> Provider<? extends T> provider(DependencyIdentifier<T> id) {
        Set<DependencyValue<T>> values = dependencies.lookup(id);
        if (values.isEmpty()) {
            // todo: error
            throw new RuntimeException("no provider for " + id);
        }

        if (values.size() > 2) {
            // todo: error
            throw new RuntimeException("multiple providers for " + id);
        }

        DependencyValue<T> value = values.iterator().next();
        Scope scope = lookupScope(value.scope().get());
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

        reflectionAssistant.fields(instance.getClass()).filter(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
            // todo: cache
            new InjectableField(this, f).inject(instance);
        });

        // todo: conform to spec
        // A method annotated with @Inject that overrides another method annotated with @Inject will only be injected once per injection request per instance.
        // A method with no @Inject annotation that overrides a method annotated with @Inject will not be injected.
        reflectionAssistant.methods(instance.getClass()).filter(m -> m.isAnnotationPresent(Inject.class)).forEach(m -> {
            // todo: cache
            new InjectableMethod(this, m).inject(instance);
        });
    }
}
