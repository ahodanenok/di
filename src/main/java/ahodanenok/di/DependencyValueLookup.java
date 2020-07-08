package ahodanenok.di;

public interface DependencyValueLookup {

    <T> DependencyValue<? extends T> lookup(Class<T> dependencyType);
}
