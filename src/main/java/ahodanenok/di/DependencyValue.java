package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;

// todo: pre destroy
// todo: are pre destroy method inheritable?

// todo: some api to describe values (builders maybe), that they could be created by container lately
// todo: must have some sort of identifier by which it will be picked for providing dependency
public interface DependencyValue<T> {

    // todo: values after bind are considered as initialized and shouldn't be configurable after that
    // todo: validate values are correct (scopes, qualifiers, types, etc)
    default void bind(DIContainer container) { }

    DependencyIdentifier<T> id();

    ScopeIdentifier scope();

    Provider<? extends T> provider();
}
