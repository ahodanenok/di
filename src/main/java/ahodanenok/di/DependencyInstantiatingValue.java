package ahodanenok.di;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DependencyInstantiatingValue<T> extends AbstractDependencyValue<T> {

    // todo: check that class is really an instantiable class

    private Class<? extends T> instanceClass;
    private InjectableConstructor<? extends T> targetConstructor;

    public DependencyInstantiatingValue(Class<T> type, Class<? extends T> instanceClass) {
        super(type);
        this.instanceClass = instanceClass;
    }

    @Override
    public Supplier<? extends T> supplier(DIContainer container) {
        return () -> {
                if (targetConstructor == null) {
                    Constructor<? extends T> constructor = resolveConstructor();
                    targetConstructor = new InjectableConstructor<>(container, constructor);
                }

                T instance = targetConstructor.inject();
                container.inject(instance);
                return instance;
        };
    }

    private Constructor<? extends T> resolveConstructor() {
        Set<Constructor<?>> constructors = Arrays.stream(instanceClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());

        // todo: errors

        if (constructors.size() > 1) {
            throw new RuntimeException("multiple injection points");
        }

        Constructor<?> constructor = null;
        if (!constructors.isEmpty()) {
            constructor = constructors.iterator().next();
        } else {
            try {
                constructor = instanceClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("no constructor");
            }
        }

        if (constructor == null) {
            throw new RuntimeException("no constructor");
        }

        return (Constructor<? extends T>) constructor;
    }
}
