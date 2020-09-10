package ahodanenok.di;

import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
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
        if (injectionPoint.getType() == Optional.class) {
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
        if (value == null && !injectionPoint.getAnnotatedTarget().isAnnotationPresent(OptionalDependency.class)) {
            throw new UnsatisfiedDependencyException(this, specifier, "not found");
        }

        return value;
    }

    private Object resolveOptional(InjectionPoint injectionPoint) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, injectionPoint.getQualifiers());
        return Optional.ofNullable(container.instance(specifier));
    }

    private Object resolveProvider(InjectionPoint injectionPoint) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, injectionPoint.getQualifiers());
        boolean optional = injectionPoint.getAnnotatedTarget().isAnnotationPresent(OptionalDependency.class);
        if (injectionPoint.getAnnotatedTarget().isAnnotationPresent(Later.class)) {
            return (Provider<Object>) () -> {
                // todo: cache provider in a field
                Provider<?> p = resolveProviderInstance(specifier, optional);
                if (p != null) {
                    return p.get();
                } else {
                    return null;
                }
            };
        } else {
            return resolveProviderInstance(specifier, optional);
        }
    }

    private Provider<?> resolveProviderInstance(ValueSpecifier<?> specifier, boolean optional) {
        Provider<?> provider = container.provider(specifier);
        if (provider == null && !optional) {
            throw new UnsatisfiedDependencyException(this, specifier, "not found");
        }

        return provider;
    }
}
