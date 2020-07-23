package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;

import javax.inject.Provider;

/**
 * Rules for reusing instances of dependencies.
 *
 * @see NotScoped
 * @see javax.inject.Singleton
 */
public interface Scope {

    ScopeIdentifier id();

    <T> T get(DependencyIdentifier<T> id, Provider<? extends T> provider);

    // todo: when scope is over, destroy it and its bindings
}
