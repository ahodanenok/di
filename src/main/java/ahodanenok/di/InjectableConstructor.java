package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;
import ahodanenok.di.qualifier.QualifierResolution;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;

public class InjectableConstructor<T> extends AbstractInjectable<T> {

    private Constructor<? extends T> constructor;
    private Consumer<AroundConstruct<T>> onConstruct;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        super(container);
        this.constructor = constructor;
    }

    public void onConstruct(Consumer<AroundConstruct<T>> onConstruct) {
        this.onConstruct = onConstruct;
    }

    @Override
    public T inject(T instance) {

        // todo: handle parameter annotations
        // todo: handle generic types
        // todo: common code here and in InjectableMethod

        Object[] args = new Object[constructor.getParameterCount()];

        // todo: research if local and anonymous classes could be injected and used
        // todo: if container doesn't have instances for enclosing class, create them for current member class only

        int i = 0;
        int paramCount = constructor.getParameterCount();
        while (i < paramCount) {
            InjectionPoint injectionPoint = new InjectionPoint(constructor, i);
            injectionPoint.setQualifiers(container.instance(QualifierResolution.class).resolve(constructor, i));
            if (onProvision != null) {
                int idx = i;
                onProvision.accept(new AroundProvision(injectionPoint, arg -> {
                    if (arg == null) {
                        arg = resolveDependency(injectionPoint);
                    }

                    args[idx] = arg;
                }));
            } else {
                args[i] = resolveDependency(injectionPoint);
            }

            i++;
        }

        if (onConstruct != null) {
            AroundConstruct<T> aroundConstruct = new AroundConstruct<>(constructor, args, this::doInject);
            onConstruct.accept(aroundConstruct);
            return aroundConstruct.getInstance();
        } else {
            return doInject(args);
        }
    }

    private T doInject(Object[] args) {
        boolean accessible = constructor.isAccessible();
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new InjectionFailedException(constructor, e);
        } finally {
            constructor.setAccessible(accessible);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + constructor + ")";
    }
}
