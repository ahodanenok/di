package ahodanenok.di;

import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.interceptor.AroundInject;
import ahodanenok.di.interceptor.InjectionPointImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.Consumer;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainer container;
    private Constructor<? extends T> constructor;
    private Consumer<AroundConstruct<T>> onConstruct;
    private Consumer<AroundInject> onInject;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        this.container = container;
        this.constructor = constructor;
    }

    public void onConstruct(Consumer<AroundConstruct<T>> onConstruct) {
        this.onConstruct = onConstruct;
    }

    public void onInject(Consumer<AroundInject> onInject) {
        this.onInject = onInject;
    }

    @Override
    public T inject(T instance) {

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
            Set<Annotation> qualifiers = container.instance(QualifierResolution.class).resolve(constructor, i);

            if (onInject != null) {
                int idx = i;

                InjectionPointImpl injectionPoint = new InjectionPointImpl();
                injectionPoint.setType(type);
                injectionPoint.setQualifiers(qualifiers);
                injectionPoint.setTarget(constructor);
                injectionPoint.setParameterIndex(idx);
                onInject.accept(new AroundInject(injectionPoint, arg -> {
                    if (arg == null && !optional[idx]) {
                        throw new UnsatisfiedDependencyException(this, DependencyIdentifier.of(type, qualifiers), "not found");
                    }

                    args[idx] = arg;
                }));
            } else {
                DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifiers);
                Object arg = container.instance(id);
                if (arg == null && !optional[i]) {
                    throw new UnsatisfiedDependencyException(this, id, "not found");
                }

                args[i] = arg;
            }

            i++;
        }

        try {
            AroundConstruct<T> aroundConstruct = new AroundConstruct<>(constructor, args);
            if (onConstruct != null) {
                //events.fire(new AroundConstructEvent<>(aroundConstruct));
                onConstruct.accept(aroundConstruct);
                return aroundConstruct.getInstance();
            } else {
                return aroundConstruct.proceed();
            }
        } catch (Exception e) {

            throw new InjectionFailedException(constructor, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + constructor + ")";
    }
}
