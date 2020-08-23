package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Set;

public class InjectionPoint {

    private Class<?> type;
//    private Set<Annotation> qualifiers = Collections.emptySet();
    private Member target;
    private AnnotatedElement annotatedTarget;
    private int parameterIndex = -1;

    public InjectionPoint(Field field) {
        this.target = field;
        this.annotatedTarget = field;
        this.type = field.getType();
    }

    public InjectionPoint(Executable target, int parameterIndex) {
        this.target = target;
        this.annotatedTarget = target.getParameters()[parameterIndex];
        this.type = target.getParameterTypes()[parameterIndex];
        this.parameterIndex = parameterIndex;
    }

    public Class<?> getType() {
        return type;
    }

//    public Set<Annotation> getQualifiers() {
//        return qualifiers;
//    }

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
