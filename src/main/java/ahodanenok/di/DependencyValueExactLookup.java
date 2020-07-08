package ahodanenok.di;

import java.util.Set;

public class DependencyValueExactLookup implements DependencyValueLookup {

    // todo: cache looked up values

    private Set<DependencyValue<?>> values;

    public DependencyValueExactLookup(Set<DependencyValue<?>> values) {
        this.values = values;
    }

    @Override
    public <T> DependencyValue<? extends T> lookup(Class<T> dependencyType) {
        for (DependencyValue<?> value : values) {
            if (value.getType() == dependencyType) {
                // todo: research what can be done not to generate warnings
                return (DependencyValue<? extends T>) value;
            }
        }

        return null;
    }
}
