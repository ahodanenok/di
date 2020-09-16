package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class InjectionPoint {

    private final Class<?> type;
    private final ParameterizedType genericType;
    private Set<Annotation> qualifiers;
    private final Member target;
    private final AnnotatedElement annotatedTarget;
    private final int parameterIndex;

    public InjectionPoint(Field field) {
        this.target = field;
        this.annotatedTarget = field;
        this.type = field.getType();
        if (field.getGenericType() instanceof ParameterizedType) {
            this.genericType = (ParameterizedType) field.getGenericType();
        } else {
            this.genericType = null;
        }
        this.parameterIndex = -1;
    }

    public InjectionPoint(Executable target, int parameterIndex) {
        this.target = target;
        this.annotatedTarget = target.getParameters()[parameterIndex];
        this.type = target.getParameterTypes()[parameterIndex];
        if (target.getGenericParameterTypes()[parameterIndex] instanceof ParameterizedType) {
            this.genericType = (ParameterizedType) target.getGenericParameterTypes()[parameterIndex];
        }else {
            this.genericType = null;
        }
        this.parameterIndex = parameterIndex;
    }

    public Class<?> getType() {
        return type;
    }

    public ParameterizedType getParameterizedType() {
        return genericType;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.unmodifiableSet(qualifiers);
    }

    public void setQualifiers(Set<Annotation> qualifiers) {
        this.qualifiers = new HashSet<>(qualifiers);
    }

    /**
     * Target of the injection
     * It will be either method, field or constructor
     */
    public Member getTarget() {
        return target;
    }

    public AnnotatedElement getAnnotatedTarget() {
        return annotatedTarget;
    }

    public int getParameterIndex() {
        if (parameterIndex < 0) {
            throw new IllegalStateException("not a parameter");
        }

        return parameterIndex;
    }
}
