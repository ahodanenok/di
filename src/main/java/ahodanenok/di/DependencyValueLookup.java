package ahodanenok.di;

import java.util.Set;

// todo: allow to use custom implementations
public interface DependencyValueLookup {

    <T> Set<DependencyValue<T>> execute(Set<DependencyValue<?>> values, DependencyIdentifier<T> id);
}
