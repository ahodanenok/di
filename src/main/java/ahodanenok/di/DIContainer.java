package ahodanenok.di;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// todo: singleton and default scopes

/**
 * Container is a coordinator between suppliers
 * Each of them manages some dependency which can be injected in other objects.
 */
// todo: identifier for suppliers (type + ?)
public final class DIContainer {

    // todo: is it necessary to keep track of injection locations
    private DependencyValueLookup dependencies;

    public DIContainer(DependencyValue<?>... suppliers) {
        this.dependencies = new DependencyValueExactLookup(Arrays.stream(suppliers).collect(Collectors.toSet()));
    }

    // todo: BindingLookup (exact type or subtypes)
    // todo: map suppliers by identifier to avoid searching through all of them
    public <T> Supplier<? extends T> supplier(Class<T> type) {
        DependencyValue<? extends T> value = dependencies.lookup(type);
        if (value != null) {
            return value.supplier(this);
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
