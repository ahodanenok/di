package ahodanenok.di;

import javax.inject.Provider;

public interface Scope {

    <T> T get(Class<T> type, Provider<? extends T> provider);
}
