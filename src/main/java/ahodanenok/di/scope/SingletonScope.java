package ahodanenok.di.scope;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {

    private ScopeIdentifier id;
    private Map<Class, Object> instances;

    public SingletonScope() {
        this.id = new ScopeIdentifier(Singleton.class);
        this.instances = new HashMap<>();
    }

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(Class<T> type, Provider<? extends T> provider) {
        T instance = (T) instances.get(type);
        if (instance == null) {
            instance = provider.get();
            instances.put(type, instance);
        }

        return instance;
    }
}
