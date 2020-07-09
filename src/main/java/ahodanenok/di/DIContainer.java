package ahodanenok.di;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// todo: singleton and default scopes

/**
 * Container is a coordinator between suppliers
 * Each of them manages some dependency which can be injected in other objects.
 */
// todo: identifier for suppliers (type + ?)
public final class DIContainer {

    private Map<String, Scope> scopes;

    // todo: is it necessary to keep track of injection locations
    private DependencyValueLookup dependencies;

    public DIContainer(DependencyValue<?>... suppliers) {
        this.dependencies = new DependencyValueExactLookup(Arrays.stream(suppliers).collect(Collectors.toSet()));
        this.scopes = new HashMap<>();
        this.scopes.put("default", new DefaultScope());
        this.scopes.put("singleton", new SingletonScope());
    }

    private Scope lookupScope(String name) {
        Scope scope = scopes.get(name);
        if (scope != null) {
            return scope;
        } else {
            throw new RuntimeException("no scope");
        }
    }

    public <T> Supplier<? extends T> supplier(Class<T> type) {
        DependencyValue<T> value = dependencies.lookup(type);
        if (value != null) {
            Scope scope = lookupScope(value.scope());
            return () -> scope.get(value.type(), value.supplier(this));
        } else {
            // todo: error
            throw new RuntimeException("no supplier");
        }
    }

    // todo: lookup by identifier
    public <T> T instance(Class<T> type) {
        Supplier<? extends T> supplier = supplier(type);
        if (supplier == null) {
            // todo: error
            throw new RuntimeException("no instance");
        }

        return supplier.get();
    }

    public void inject(Object object) {
        // todo: implement
    }
}
