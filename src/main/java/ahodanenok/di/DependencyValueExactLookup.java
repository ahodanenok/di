package ahodanenok.di;

import java.util.HashSet;
import java.util.Set;

public class DependencyValueExactLookup implements DependencyValueLookup {

    // todo: cache looked up values

    private Set<DependencyValue<?>> values;

    public DependencyValueExactLookup() {
        this(new HashSet<>());
    }

    public DependencyValueExactLookup(Set<DependencyValue<?>> values) {
        this.values = values;
    }

    @Override
    public <T> DependencyValue<T> lookup(Class<T> dependencyType) {
        for (DependencyValue<?> value : values) {
            if (value.type() == dependencyType) {
                // todo: research what can be done not to generate warnings
                return (DependencyValue<T>) value;
            }
        }

        return null;
    }
}
