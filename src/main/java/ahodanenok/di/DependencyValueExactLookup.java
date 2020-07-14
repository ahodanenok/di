package ahodanenok.di;

import java.util.HashSet;
import java.util.Set;

public class DependencyValueExactLookup implements DependencyValueLookup {

    // todo: cache looked up values

    private Set<DependencyValue<?>> values;

    public DependencyValueExactLookup(Set<DependencyValue<?>> values) {
        this.values = values;
    }

    @Override
    public <T> Set<DependencyValue<T>> lookup(DependencyIdentifier<T> id) {
        Set<DependencyValue<T>> matching = new HashSet<>();
        for (DependencyValue<?> v : values) {
            if (id.equals(v.id())) {
                @SuppressWarnings("unchecked") // if ids are equals, type is T
                DependencyValue<T> matched = (DependencyValue<T>) v;
                matching.add(matched);
            }
        }

        return matching;
    }
}
