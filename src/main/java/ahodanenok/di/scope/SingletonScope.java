package ahodanenok.di.scope;

import ahodanenok.di.value.Value;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @see Singleton
 */
public class SingletonScope implements Scope {

    private Map<Value<?>, Object> instances;

    public SingletonScope() {
        this.instances = new HashMap<>();
    }

    @Override
    public ScopeIdentifier id() {
        return ScopeIdentifier.SINGLETON;
    }

    @Override
    public <T> T get(Value<T> value) {
        @SuppressWarnings("unchecked") // provider for id returns some subtype of T or T itself
        T instance = (T) instances.get(value);
        if (instance == null) {
            instance = value.provider().get();
            instances.put(value, instance);
        }

        return instance;
    }
}
