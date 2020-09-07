package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectableMethod extends AbstractInjectable<Object> {

    private Method method;

    public InjectableMethod(DIContainer container, Method method) {
        super(container);
        this.method = method;
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

        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameterCount(); i++) {
            InjectionPoint injectionPoint = new InjectionPoint(method, i);
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
