package ahodanenok.di;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SingletonScope implements Scope {

    private Map<Class, Object> instances;

    public SingletonScope() {
        this.instances = new HashMap<>();
    }

    @Override
    public <T> T get(Class<T> type, Supplier<? extends T> supplier) {
        T instance = (T) instances.get(type);
        if (instance == null) {
            instance = supplier.get();
            instances.put(type, instance);
        }

        return instance;
    }
}
