package ahodanenok.di.interceptor;

import java.lang.reflect.*;

public class InjectionPoint {

    // todo: make fields final
    private Class<?> type;
    private ParameterizedType genericType;
    private Member target;
    private AnnotatedElement annotatedTarget;
    private int parameterIndex = -1;

    public InjectionPoint(Field field) {
        this.target = field;
        this.annotatedTarget = field;
        this.type = field.getType();
        if (field.getGenericType() instanceof ParameterizedType) {
            this.genericType = (ParameterizedType) field.getGenericType();
        }
    }

    public InjectionPoint(Executable target, int parameterIndex) {
        this.target = target;
        this.annotatedTarget = target.getParameters()[parameterIndex];
        this.type = target.getParameterTypes()[parameterIndex];
        if (target.getGenericParameterTypes()[parameterIndex] instanceof ParameterizedType) {
            this.genericType = (ParameterizedType) target.getGenericParameterTypes()[parameterIndex];
        }
        this.parameterIndex = parameterIndex;
    }

    public Class<?> getType() {
        return type;
    }

    public ParameterizedType getParameterizedType() {
        return genericType;
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
