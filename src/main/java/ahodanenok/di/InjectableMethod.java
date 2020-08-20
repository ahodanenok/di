package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundInject;
import ahodanenok.di.interceptor.InjectionPointImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Consumer;

public class InjectableMethod implements Injectable<Object> {

    private DIContainer container;
    private Method method;
    private Consumer<AroundInject> onInject;

    public InjectableMethod(DIContainer container, Method method) {
        this.container = container;
        this.method = method;
    }

    public void setOnInject(Consumer<AroundInject> onInject) {
        this.onInject = onInject;
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
            Set<Annotation> qualifiers = container.instance(QualifierResolution.class).resolve(method, i);

            if (onInject != null) {
                int idx = i;

                InjectionPointImpl injectionPoint = new InjectionPointImpl();
                injectionPoint.setType(type);
                injectionPoint.setQualifiers(qualifiers);
                injectionPoint.setTarget(method);
                injectionPoint.setParameterIndex(idx);
                onInject.accept(new AroundInject(injectionPoint, arg -> {
                    if (arg == null && !optional[idx]) {
                        throw new UnsatisfiedDependencyException(this, ValueSpecifier.of(type, qualifiers), "not found");
                    }

                    args[idx] = arg;
                }));
            } else {
                ValueSpecifier<?> specifier = ValueSpecifier.of(type, qualifiers);
                Object arg = container.instance(specifier);
                if (arg == null && !optional[i]) {
                    throw new UnsatisfiedDependencyException(this, specifier, "not found");
                }

                args[i] = arg;
            }

            i++;
        }

        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            // todo: expose invocation as an object, so clients could do something before and after invocation
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
