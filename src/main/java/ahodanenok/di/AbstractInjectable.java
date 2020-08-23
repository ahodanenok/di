package ahodanenok.di;

import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundProvision;
import ahodanenok.di.interceptor.InjectionPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
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

        Class<?> lookupType = injectionPoint.getType();
        if (injectionPoint.getType() == Optional.class) {
            System.out.println("OPTIONAL");
            lookupType = (Class<?>) injectionPoint.getParameterizedType().getActualTypeArguments()[0];
            System.out.println(lookupType);
        }

        ValueSpecifier<?> specifier = ValueSpecifier.of(lookupType, qualifiers);
        Object value = container.instance(specifier);
        if (injectionPoint.getType() == Optional.class) {
            value = Optional.ofNullable(value);
        }

        if (value == null && !injectionPoint.getAnnotatedTarget().isAnnotationPresent(OptionalDependency.class)) {
            throw new UnsatisfiedDependencyException(this, specifier, "not found");
        }

        return value;
    }
}
