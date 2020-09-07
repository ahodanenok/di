package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.util.*;

public class MutableValueMetadata {

    protected String name;
    protected ScopeIdentifier scope;
    protected String profilesCondition;
    protected Boolean isPrimary;
    protected Boolean isDefault;
    protected Boolean eager;
    protected Integer initializationPhase;
    protected Set<Annotation> qualifiers;
    protected Set<Annotation> stereotypes;
    protected Boolean interceptor;
    protected Set<Annotation> interceptorBindings;
    protected Integer priority;

    public void overrideWith(MutableValueMetadata metadata) {
        if (metadata.name != null) {
            setName(metadata.name);
        }

        if (metadata.scope != null) {
            setScope(metadata.scope);
        }

        if (metadata.profilesCondition != null) {
            setProfilesCondition(metadata.profilesCondition);
        }

        if (metadata.isPrimary != null) {
            setPrimary(metadata.isPrimary);
        }

        if (metadata.isDefault != null) {
            setDefault(metadata.isDefault);
        }

        if (metadata.eager != null) {
            setEager(metadata.eager);
        }

        if (metadata.initializationPhase != null) {
            setInitializationPhase(metadata.initializationPhase);
        }

        if (metadata.qualifiers != null) {
            setQualifiers(metadata.qualifiers);
        }

        if (metadata.stereotypes != null) {
            setStereotypes(metadata.stereotypes);
        }

        if (metadata.interceptor != null) {
            setInterceptor(metadata.interceptor);
        }

        if (metadata.interceptorBindings != null) {
            setInterceptorBindings(metadata.interceptorBindings);
        }

        if (metadata.priority != null) {
            setPriority(metadata.priority);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            name = name.trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name can't be empty");
            }
        }

        this.name = name;
    }

    public ScopeIdentifier getScope() {
        return scope != null ? scope : ScopeIdentifier.NOT_SCOPED;
    }

    public void setScope(ScopeIdentifier scope) {
        Objects.requireNonNull(scope);
        this.scope = scope;
    }

    public String getProfilesCondition() {
        return profilesCondition;
    }

    public void setProfilesCondition(String profilesCondition) {
        if (profilesCondition != null) {
            profilesCondition = profilesCondition.trim();
            if (profilesCondition.isEmpty()) {
                throw new IllegalArgumentException("Profiles can't be empty");
            }
        }

        this.profilesCondition = profilesCondition;
    }

    public int getInitializationPhase() {
        return initializationPhase != null ? initializationPhase : 0;
    }

    public void setInitializationPhase(int phase) {
        this.initializationPhase = phase;
    }

    public int getPriority() {
        return priority != null ? priority : 0;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isPrimary() {
        return isPrimary != null ? isPrimary : false;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean isDefault() {
        return isDefault != null ? isDefault : false;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isEager() {
        return eager != null ? eager : false;
    }

    public void setEager(boolean eager) {
        this.eager = eager;
    }

    public boolean isInterceptor() {
        return interceptor != null ? interceptor : false;
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }

    public Set<Annotation> getQualifiers() {
        return qualifiers != null ? qualifiers : Collections.emptySet();
    }

    public void setQualifiers(Annotation... qualifiers) {
        setQualifiers(new HashSet<>(Arrays.asList(qualifiers)));
    }

    public void setQualifiers(Set<Annotation> qualifiers) {
        if (qualifiers == null) {
            throw new IllegalArgumentException("Qualifiers list can't be null");
        }

        this.qualifiers = qualifiers;
    }

    public Set<Annotation> getStereotypes() {
        return stereotypes != null ? stereotypes : Collections.emptySet();
    }

    public void setStereotypes(Set<Annotation> stereotypes) {
        if (stereotypes == null) {
            throw new IllegalArgumentException("Stereotypes list can't be null");
        }

        this.stereotypes = stereotypes;
    }

    public Set<Annotation> getInterceptorBindings() {
        return interceptorBindings != null ? interceptorBindings : Collections.emptySet();
    }

    public void setInterceptorBindings(Set<Annotation> bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("Bindings list can't be null");
        }

        this.interceptorBindings = bindings;
    }
}
