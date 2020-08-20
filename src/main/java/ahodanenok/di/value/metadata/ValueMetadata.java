package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public abstract class ValueMetadata {

    protected Class<?> type;
    protected String name;
    protected ScopeIdentifier scope = ScopeIdentifier.NOT_SCOPED;
    protected boolean isPrimary = false;
    protected boolean isDefault = false;
    protected boolean eager = false;
    protected Set<Annotation> qualifiers = Collections.emptySet();
    protected Set<Annotation> stereotypes = Collections.emptySet();

    protected boolean interceptor = false;
    protected Set<Annotation> interceptorBindings = Collections.emptySet();


    public ValueMetadata(Class<?> type) {
        this.type = type;
    }

    public Class<?> valueType() {
        return type;
    };

    public String getName() {
        return name;
    }

    public ScopeIdentifier getScope() {
        return scope;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isEager() {
        return eager;
    }

    public boolean isInterceptor() {
        return interceptor;
    }

    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    public Set<Annotation> getStereotypes() {
        return stereotypes;
    }

    public Set<Annotation> getInterceptorBindings() {
        return interceptorBindings;
    }

    public Method getAroundInvoke() {
        return null;
    }

    public Method getAroundConstruct() {
        return null;
    }

    public Method getPostConstruct() {
        return null;
    }

    public Method getPreDestroy() {
        return null;
    }
}
