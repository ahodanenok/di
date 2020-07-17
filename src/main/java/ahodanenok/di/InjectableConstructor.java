package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InjectableConstructor<T> implements Injectable<T> {

    private DIContainer container;
    private Constructor<? extends T> constructor;
    private QualifierResolution qualifierResolution;

    public InjectableConstructor(DIContainer container, Constructor<? extends T> constructor) {
        this.container = container;
        this.constructor = constructor;
        // todo: get QualifierResolution from container
        this.qualifierResolution = new AnnotatedQualifierResolution();
    }

    @Override
    public T inject(T instance) {
        try {

            // todo: handle parameter annotations
            // todo: handle generic types
            // todo: common code here and in InjectableMethod

            // todo: cache
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            boolean[] optional = new boolean[constructor.getParameterCount()];
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (int j = 0; j < parameterAnnotations[i].length; j++) {
                    if (parameterAnnotations[i][j].annotationType().equals(OptionalDependency.class)) {
                        optional[i] = true;
                        break;
                    }
                }
            }

            int i = 0;
            Object[] args = new Object[constructor.getParameterCount()];
            for (Class<?> type : constructor.getParameterTypes()) {
                Annotation qualifier = qualifierResolution.resolve(constructor, i);
                DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifier);
                Object arg = container.instance(id);
                if (arg == null && !optional[i]) {
                    throw new UnsatisfiedDependencyException(this, id, "not found");
                }

                args[i++] = arg;
            }

            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InjectionFailedException(constructor, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + constructor + ")";
    }
}
