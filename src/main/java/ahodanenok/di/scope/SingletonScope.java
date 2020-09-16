package ahodanenok.di.scope;

import ahodanenok.di.value.ManagedValue;
import ahodanenok.di.value.Value;

import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @Override
    public void destroy() {
        // todo: make copy of instances and assign to it empty map
        for (Map.Entry<Value<?>, Object> entry : instances.entrySet()) {
            System.out.println(entry.getKey().getClass());
            if (entry.getKey() instanceof ManagedValue) {
                Method preDestroyMethod = ((ManagedValue) entry.getKey()).getPreDestroyMethod();
                if (preDestroyMethod != null) {
                    boolean accessible = preDestroyMethod.isAccessible();
                    try {
                        preDestroyMethod.setAccessible(true);
                        preDestroyMethod.invoke(entry.getValue());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        // If the method throws an unchecked exception it is ignored.
                    } finally {
                        preDestroyMethod.setAccessible(accessible);
                    }
                }
            }
        }
    }
}
