package ahodanenok.di.scope;

import javax.inject.Provider;

public interface Scope {

    ScopeIdentifier id();

    <T> T get(Class<T> type, Provider<? extends T> provider);
}
