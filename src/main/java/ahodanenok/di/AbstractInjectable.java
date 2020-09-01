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

        // todo: get rid of type checking
        Set<Annotation> qualifiers;
        if (injectionPoint.getTarget() instanceof Field) {
            qualifiers = container.instance(QualifierResolution.class).resolve((Field) injectionPoint.getTarget());
        } else {
            qualifiers = container.instance(QualifierResolution.class).resolve((Executable) injectionPoint.getTarget(), injectionPoint.getParameterIndex());
        }

        Object value;
        if (injectionPoint.getType() == Optional.class) {
            value = resolveOptional(injectionPoint, qualifiers);
        } else if (injectionPoint.getType() == Provider.class) {
            value = resolveProvider(injectionPoint, qualifiers);
        } else {
            value = resolveObject(injectionPoint, qualifiers);
        }

        return value;
    }

    private Object resolveObject(InjectionPoint injectionPoint, Set<Annotation> qualifiers) {
        ValueSpecifier<?> specifier = ValueSpecifier.of(injectionPoint.getType(), qualifiers);
        Object value = container.instance(specifier);
        if (value == null && !injectionPoint.getAnnotatedTarget().isAnnotationPresent(OptionalDependency.class)) {
            throw new UnsatisfiedDependencyException(this, specifier, "not found");
        }

        return value;
    }

    private Object resolveOptional(InjectionPoint injectionPoint, Set<Annotation> qualifiers) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, qualifiers);
        return Optional.ofNullable(container.instance(specifier));
    }

    private Object resolveProvider(InjectionPoint injectionPoint, Set<Annotation> qualifiers) {
        Class<?> lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, qualifiers);
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
