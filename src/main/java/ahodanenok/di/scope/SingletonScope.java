package ahodanenok.di.scope;

import ahodanenok.di.DependencyIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {

    private ScopeIdentifier id;
    private Map<DependencyIdentifier<?>, Object> instances;

    public SingletonScope() {
        this.id = ScopeIdentifier.of(Singleton.class);
        this.instances = new HashMap<>();
    }

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(DependencyIdentifier<T> id, Provider<? extends T> provider) {
        T instance = (T) instances.get(id);
        if (instance == null) {
            instance = provider.get();
            instances.put(id, instance);
        }

        return instance;
    }
}
