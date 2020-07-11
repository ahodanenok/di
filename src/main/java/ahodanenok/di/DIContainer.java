package ahodanenok.di;

import ahodanenok.di.scope.DefaultScope;
import ahodanenok.di.scope.Scope;
import ahodanenok.di.scope.SingletonScope;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.stream.Collectors;

// todo: handle javax.inject.Named
// todo: handle javax.inject.Qualifier
// todo: logging

/**
 * Container is a coordinator between providers
 * Each of them manages some dependency which can be injected in other objects.
 */
// todo: identifier for suppliers (type + ?)
public final class DIContainer {

    private Map<String, Scope> scopes;
    private ReflectionAssistant reflectionAssistant;

    // todo: is it necessary to keep track of injection locations
    private DependencyValueLookup dependencies;

    public DIContainer(DependencyValue<?>... values) {
        this.reflectionAssistant = new ReflectionAssistant();
        this.dependencies = new DependencyValueExactLookup(Arrays.stream(values).collect(Collectors.toSet()));
        this.scopes = new HashMap<>();

        // custom scopes with the same identifier override built-in scopes
        Set<Scope> scopes = new HashSet<>();
        scopes.add(new DefaultScope());
        scopes.add(new SingletonScope());
        // todo: load custom scopes

        for (Scope scope : scopes) {
            this.scopes.put(scope.id().get(), scope);
        }
    }

    private Scope lookupScope(String name) {
        Scope scope = scopes.get(name);
        if (scope != null) {
            return scope;
        } else {
            throw new RuntimeException("no scope");
        }
    }

    public <T> Provider<? extends T> provider(DependencyIdentifier<T> id) {
        DependencyValue<T> value = dependencies.lookup(id);
        if (value != null) {
            Scope scope = lookupScope(value.scope().get());
            return () -> scope.get(value.id(), value.provider(this));
        } else {
            // todo: error
            throw new RuntimeException("no supplier");
        }
    }

    // todo: lookup by identifier
    public <T> T instance(DependencyIdentifier<T> id) {
        Provider<? extends T> provider = provider(id);
        if (provider == null) {
            // todo: error
            throw new RuntimeException("no instance");
        }

        return provider.get();
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
