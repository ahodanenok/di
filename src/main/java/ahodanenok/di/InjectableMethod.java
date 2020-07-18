package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectableMethod implements Injectable<Object> {

    private DIContainer container;
    private Method method;
    private QualifierResolution qualifierResolution;

    public InjectableMethod(DIContainer container, Method method) {
        this.container = container;
        this.method = method;
        // todo: get QualifierResolution from container
        this.qualifierResolution = new AnnotatedQualifierResolution();
    }

    @Override
    public Object inject(Object instance) {

        // todo: common code here and in InjectableConstructor

        // todo: conform to spec
        // Injectable methods:
        // are annotated with @Inject.
        // are not abstract.
        // do not declare type parameters of their own.
        // may return a result
        // may have any otherwise valid name.
        // accept zero or more dependencies as arguments.

        // todo: cache
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        boolean[] optional = new boolean[method.getParameterCount()];
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j].annotationType().equals(OptionalDependency.class)) {
                    optional[i] = true;
                    break;
                }
            }
        }

        int i = 0;
        Object[] args = new Object[method.getParameterCount()];
        for (Class<?> type : method.getParameterTypes()) {
            Annotation qualifier = qualifierResolution.resolve(method, i);

            DependencyIdentifier<?> id = DependencyIdentifier.of(type, qualifier);
            Object arg = container.instance(id);
            if (arg == null && !optional[i]) {
                throw new UnsatisfiedDependencyException(this, id, "not found");
            }

            args[i++] = arg;
        }

        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InjectionFailedException(method, e);
        } finally {
            method.setAccessible(accessible);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + method + ")";
    }
}
