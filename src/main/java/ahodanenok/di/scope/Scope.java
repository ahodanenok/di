package ahodanenok.di.scope;

import ahodanenok.di.DependencyValue;

/**
 * Rules for reusing instances of dependencies.
 *
 * @see NotScoped
 * @see javax.inject.Singleton
 */
public interface Scope {

    ScopeIdentifier id();

    <T> T get(DependencyValue<T> id);

    // todo: when scope is over, destroy it and its bindings
}
