package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;

import javax.inject.Provider;

public interface Scope {

    ScopeIdentifier id();

    <T> T get(DependencyIdentifier<T> id, Provider<? extends T> provider);
}
