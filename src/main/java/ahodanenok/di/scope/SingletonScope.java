package ahodanenok.di.scope;

import ahodanenok.di.DependencyValue;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @see Singleton
 */
public class SingletonScope implements Scope {

    private ScopeIdentifier id;
    private Map<DependencyValue<?>, Object> instances;

    public SingletonScope() {
        this.id = ScopeIdentifier.of(Singleton.class);
        this.instances = new HashMap<>();
    }

    @Override
    public ScopeIdentifier id() {
        return id;
    }

    @Override
    public <T> T get(DependencyValue<T> value) {
        @SuppressWarnings("unchecked") // provider for id returns some subtype of T or T itself
        T instance = (T) instances.get(value);
        if (instance == null) {
            instance = value.provider().get();
            instances.put(value, instance);
        }

        return instance;
    }
}
