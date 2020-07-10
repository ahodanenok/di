package ahodanenok.di;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {

    private Map<Class, Object> instances;

    public SingletonScope() {
        this.instances = new HashMap<>();
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
