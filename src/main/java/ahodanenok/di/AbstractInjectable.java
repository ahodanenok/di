package ahodanenok.di;

import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;
import ahodanenok.di.value.ValueSpecifier;

import javax.inject.Provider;
import java.util.function.Consumer;

public abstract class AbstractInjectable<T> implements Injectable<T> {

    protected final DIContainer container;
    protected Consumer<AroundProvision> onProvision;

    public AbstractInjectable(DIContainer container) {
        this.container = container;
    }

    public void setOnProvision(Consumer<AroundProvision> onProvision) {
        this.onProvision = onProvision;
    }

    protected Object resolveDependency(InjectionPoint injectionPoint) {
        Object value;
        if (injectionPoint.getType() == java.util.Optional.class) {
            value = resolveOptional(injectionPoint);
        } else if (injectionPoint.getType() == Provider.class) {
            value = resolveProvider(injectionPoint);
        } else {
            value = resolveObject(injectionPoint);
        }

        return value;
    }

    private Object resolveObject(InjectionPoint injectionPoint) {
        ValueSpecifier<?> specifier = ValueSpecifier.of(injectionPoint.getType(), injectionPoint.getQualifiers());
        Object value = container.instance(specifier);
        if (value == null && !injectionPoint.getAnnotatedTarget().isAnnotationPresent(Optional.class)) {
            throw new UnsatisfiedDependencyException(
                    String.format("Value with specifier %s hasn't been found in the container for injection point '%s'",
                            specifier, injectionPoint.getTarget()));
        }

        return value;
    }

    private Object resolveOptional(InjectionPoint injectionPoint) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, injectionPoint.getQualifiers());
        return java.util.Optional.ofNullable(container.instance(specifier));
    }

    private Object resolveProvider(InjectionPoint injectionPoint) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, injectionPoint.getQualifiers());
        boolean optional = injectionPoint.getAnnotatedTarget().isAnnotationPresent(Optional.class);
        if (injectionPoint.getAnnotatedTarget().isAnnotationPresent(Later.class)) {
            return new Provider<Object>() {

                private Provider<?> resolvedProvider;

                @Override
                public Object get() {
                    if (resolvedProvider == null) {
                        Provider<?> p = resolveProviderInstance(injectionPoint, specifier, optional);
                        if (p != null) {
                            resolvedProvider = p;
                        } else {
                            resolvedProvider = () -> null;
                        }
                    }

                    return resolvedProvider.get();
                }
            };
        } else {
            return resolveProviderInstance(injectionPoint, specifier, optional);
        }
    }

    private Provider<?> resolveProviderInstance(InjectionPoint injectionPoint, ValueSpecifier<?> specifier, boolean optional) {
        Provider<?> provider = container.provider(specifier);
        if (provider == null && !optional) {
            throw new UnsatisfiedDependencyException(
                    String.format("Value provider with specifier '%s' hasn't been found in the container for injection point '%s'",
                            specifier, injectionPoint.getTarget()));
        }

        return provider;
    }
}
