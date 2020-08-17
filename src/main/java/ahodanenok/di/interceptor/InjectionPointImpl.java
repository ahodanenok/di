package ahodanenok.di.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Set;

public final class InjectionPointImpl implements InjectionPoint {

    private Class<?> type;
    private Set<Annotation> qualifiers = Collections.emptySet();
    private Member target;
    private int parameterIndex = -1;

    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public void setQualifiers(Set<Annotation> qualifiers) {
        this.qualifiers = qualifiers;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    public void setTarget(Member target) {
        this.target = target;
    }

    /**
     * Target of the injection
     * It will be either method, field or constructor
     */
    public Member getTarget() {
        return target;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    @Override
    public int getParameterIndex() {
        if (parameterIndex < 0) {
            throw new IllegalStateException("not a parameter");
        }

        return parameterIndex;
    }
}
