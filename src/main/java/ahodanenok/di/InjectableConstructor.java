package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.interceptor.AroundConstructObserver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainer container;
    private Constructor<? extends T> constructor;
    private AroundConstructObserver observer;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        this.container = container;
        this.constructor = constructor;
    }

    public void setAroundConstructObserver(AroundConstructObserver observer) {
        this.observer = observer;
    }

    @Override
    public T inject(T instance) {
        try {

            // todo: handle parameter annotations
            // todo: handle generic types
            // todo: common code here and in InjectableMethod

            // todo: cache
            boolean[] optional = new boolean[constructor.getParameterCount()];
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                optional[i] = ReflectionAssistant.parameterAnnotations(constructor, i, ReflectionAssistant.AnnotationPresence.DIRECTLY)
                        .anyMatch(a -> a.annotationType().equals(OptionalDependency.class));
            }

            Object[] args = new Object[constructor.getParameterCount()];
            Class<?>[] types = constructor.getParameterTypes();

            // todo: research if local and anonymous classes could be injected and used
            // todo: if container doesn't have instances for enclosing class, create them for current member class only

            int i = 0;
            int paramCount = constructor.getParameterCount();
            while (i < paramCount) {
                Class<?> type = types[i];

                Set<Annotation> qualifiers = container.qualifierResolution().resolve(constructor, i);
                DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifiers);
                Object arg = container.instance(id);
                if (arg == null && !optional[i]) {
                    throw new UnsatisfiedDependencyException(this, id, "not found");
                }

                args[i++] = arg;
            }

            AroundConstruct<T> aroundConstruct = new AroundConstruct<>(constructor, args);
            if (observer != null) {
                observer.observe(aroundConstruct);
                return aroundConstruct.getInstance();
            } else {
                return aroundConstruct.proceed();
            }
        } catch (ReflectiveOperationException e) {
            throw new InjectionFailedException(constructor, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + constructor + ")";
    }
}
