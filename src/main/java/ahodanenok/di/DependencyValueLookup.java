package ahodanenok.di;

public interface DependencyValueLookup {

    <T> DependencyValue<T> lookup(Class<T> dependencyType);
}
