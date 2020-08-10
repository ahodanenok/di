package ahodanenok.di;

import ahodanenok.di.event.AroundConstructEvent;
import ahodanenok.di.event.Events;
import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundConstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainer container;
    private Constructor<? extends T> constructor;
    private Events events;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        this.container = container;
        this.constructor = constructor;
        this.events = container.instance(Events.class);
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
            DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifiers);
            Object arg = container.instance(id);
            if (arg == null && !optional[i]) {
                throw new UnsatisfiedDependencyException(this, id, "not found");
            }

            args[i++] = arg;
        }


        AroundConstruct<T> aroundConstruct = new AroundConstruct<>(constructor, args);

        try {

            if (events != null) {
//                context.getAroundConstructObserver().observe(aroundConstruct);
                events.fire(new AroundConstructEvent<>(aroundConstruct));
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
