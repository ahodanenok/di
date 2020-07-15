package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;

// todo: some api to describe values (builders maybe), that they could be created by container lately
// todo: must have some sort of identifier by which it will be picked for providing dependency
public interface DependencyValue<T> {

    default void bind(DIContainer container) { }

    DependencyIdentifier<T> id();

    ScopeIdentifier scope();

    Provider<? extends T> provider();
}
