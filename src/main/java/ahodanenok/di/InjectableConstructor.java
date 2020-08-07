package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundConstruct;
import ahodanenok.di.interceptor.AroundConstructObserver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainerContext context;
    private Constructor<? extends T> constructor;

    public InjectableConstructor(DIContainerContext context, Constructor<? extends T> constructor) {
        this.context = context;
        this.constructor = constructor;
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

                Set<Annotation> qualifiers = context.getQualifierResolution().resolve(constructor, i);
                DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifiers);
                Object arg = context.getContainer().instance(id);
                if (arg == null && !optional[i]) {
                    throw new UnsatisfiedDependencyException(this, id, "not found");
                }

                args[i++] = arg;
            }

            AroundConstruct<T> aroundConstruct = new AroundConstruct<>(constructor, args);

            if (context.getAroundConstructObserver() != null) {
                context.getAroundConstructObserver().observe(aroundConstruct);
                return aroundConstruct.getInstance();
            } else {
                return aroundConstruct.proceed();
            }
        } catch (Exception e) {
            if (e instanceof UnsatisfiedDependencyException) {
                throw (UnsatisfiedDependencyException) e;
            }

            throw new InjectionFailedException(constructor, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + constructor + ")";
    }
}
