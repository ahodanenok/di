package ahodanenok.di;

import javax.inject.Provider;

public class DefaultScope implements Scope {

    @Override
    public <T> T get(Class<T> type, Provider<? extends T> provider) {
        return provider.get();
    }
}
