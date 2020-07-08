package ahodanenok.di;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// todo: BindingLookup (exact)
// todo: singleton and default scopes

/**
 * Container is a coordinator between suppliers
 * Each of them manages some dependency which can be injected in other objects.
 */
// todo: identifier for suppliers (type + ?)
public final class DIContainer {

    // todo: is it necessary to keep track of injection locations
    private Set<DependencyValue> suppliers;

    public DIContainer(DependencyValue<?>... suppliers) {
        this.suppliers = Arrays.stream(suppliers).collect(Collectors.toSet());
    }

    // todo: BindingLookup (exact type or subtypes)
    // todo: map suppliers by identifier to avoid searching through all of them
    public <T> Supplier<T> supplier(Class<? extends T> type) {
        for (DependencyValue<?> supplier : suppliers) {
            if (supplier.getType() == type) {
                // todo: research what can be done not to generate warnings
                return (Supplier<T>) supplier.supplier(this);
            }
        }

        return null;
    }

    // todo: lookup by identifier
    public <T> T instance(Class<? extends T> type) {
        Supplier<T> supplier = supplier(type);
        if (supplier == null) {
            throw new RuntimeException("no instance");
        }

        return supplier.get();
    }

    public void inject(Object object) {
        // todo: implement
    }
}
