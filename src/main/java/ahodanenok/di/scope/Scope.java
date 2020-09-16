package ahodanenok.di.scope;

import ahodanenok.di.value.Value;

import java.lang.annotation.Annotation;

/**
 * Rules for reusing instances of dependencies.
 *
 * @see NotScoped
 * @see javax.inject.Singleton
 */
public interface Scope {

    ScopeIdentifier id();

    <T> T get(Value<T> id);

    default void destroy() { }
}
