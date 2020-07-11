package ahodanenok.di;

import java.util.Set;

public class DependencyValueExactLookup implements DependencyValueLookup {

    // todo: cache looked up values

    private Set<DependencyValue<?>> values;

    public DependencyValueExactLookup(Set<DependencyValue<?>> values) {
        this.values = values;
    }

    @Override
    public <T> DependencyValue<T> lookup(DependencyIdentifier<T> id) {
        for (DependencyValue<?> value : values) {
            if (id.equals(value.id())) {
                // todo: research what can be done not to generate warnings
                return (DependencyValue<T>) value;
            }
        }

        return null;
    }
}
