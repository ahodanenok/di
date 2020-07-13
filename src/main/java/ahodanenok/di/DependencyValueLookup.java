package ahodanenok.di;

import java.util.Set;

// todo: allow to use custom implementations
public interface DependencyValueLookup {

    <T> Set<DependencyValue<T>> lookup(DependencyIdentifier<T> id);
}
