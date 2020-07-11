package ahodanenok.di;

// todo: allow to use custom implementations
public interface DependencyValueLookup {

    <T> DependencyValue<T> lookup(DependencyIdentifier<T> id);
}
