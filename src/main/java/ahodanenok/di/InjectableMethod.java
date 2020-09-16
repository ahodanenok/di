package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;
import ahodanenok.di.qualifier.QualifierResolution;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class InjectableMethod extends AbstractInjectable<Object> {

    private final Method method;

    public InjectableMethod(DIContainer container, Method method) {
        super(container);

        if (Modifier.isAbstract(method.getModifiers())) {
            throw new IllegalArgumentException("Method is abstract");
        }

        // todo: do not declare type parameters of their own.

        this.method = method;
    }

    @Override
    public Object inject(Object instance) {
        // todo: handle generics

        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameterCount(); i++) {
            InjectionPoint injectionPoint = new InjectionPoint(method, i);
            injectionPoint.setQualifiers(container.instance(QualifierResolution.class).resolve(method, i));
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

        try {
            return ReflectionAssistant.invoke(method, instance, args);
        } catch (Exception e) {
            throw new InjectionFailedException(method, e);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + method + ")";
    }
}
