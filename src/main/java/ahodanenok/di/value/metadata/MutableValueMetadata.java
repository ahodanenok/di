package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.util.*;

public class MutableValueMetadata implements ValueMetadata {

    private MutableValueMetadata parent;

    protected String name;
    protected ScopeIdentifier scope = ScopeIdentifier.NOT_SCOPED;
    protected String profilesCondition;
    protected boolean isPrimary = false;
    protected boolean isDefault = false;
    protected boolean eager = false;
    protected int initializationPhase;
    protected Set<Annotation> qualifiers = Collections.emptySet();
    protected Set<Annotation> stereotypes = Collections.emptySet();
    protected boolean interceptor = false;
    protected Set<Annotation> interceptorBindings = Collections.emptySet();
    protected int priority;

    private Set<String> overridden = new HashSet<>();

    public MutableValueMetadata getParent() {
        return parent;
    }

    public void setParent(MutableValueMetadata parent) {
        this.parent = parent;
    }

    public String getName() {
        if (parent != null && !isOverridden("name")) {
            return parent.getName();
        } else {
            return name;
        }
    }

    public void setName(String name) {
        if (name != null) {
            name = name.trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name can't be empty");
            }
        }

        this.name = name;
        setOverridden("name");
    }

    public ScopeIdentifier getScope() {
        if (parent != null && !isOverridden("scope")) {
            return parent.getScope();
        } else {
            return scope;
        }
    }

    public void setScope(ScopeIdentifier scope) {
        Objects.requireNonNull(scope);
        this.scope = scope;
        setOverridden("scope");
    }

    public String getProfilesCondition() {
        if (parent != null && !isOverridden("profilesCondition")) {
            return parent.getProfilesCondition();
        } else {
            return profilesCondition;
        }
    }

    public void setProfilesCondition(String profilesCondition) {
        if (profilesCondition != null) {
            profilesCondition = profilesCondition.trim();
            if (profilesCondition.isEmpty()) {
                throw new IllegalArgumentException("Profiles can't be empty");
            }
        }

        this.profilesCondition = profilesCondition;
        setOverridden("profilesCondition");
    }

    public int getInitializationPhase() {
        if (parent != null && !isOverridden("initializationPhase")) {
            return parent.getInitializationPhase();
        } else {
            return initializationPhase;
        }
    }

    public void setInitializationPhase(int phase) {
        this.initializationPhase = phase;
        setOverridden("initializationPhase");
    }

    public int getPriority() {
        if (parent != null && !isOverridden("priority")) {
            return parent.getPriority();
        } else {
            return priority;
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
        setOverridden("priority");
    }

    public boolean isPrimary() {
        if (parent != null && !isOverridden("isPrimary")) {
            return parent.isPrimary();
        } else {
            return isPrimary;
        }
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
        setOverridden("isPrimary");
    }

    public boolean isDefault() {
        if (parent != null && !isOverridden("isDefault")) {
            return parent.isDefault();
        } else {
            return isDefault;
        }
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        setOverridden("isDefault");
    }

    public boolean isEager() {
        if (parent != null && !isOverridden("eager")) {
            return parent.isEager();
        } else {
            return eager;
        }
    }

    public void setEager(boolean eager) {
        this.eager = eager;
        setOverridden("eager");
    }

    public boolean isInterceptor() {
        if (parent != null && !isOverridden("interceptor")) {
            return parent.isInterceptor();
        } else {
            return interceptor;
        }
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
        setOverridden("interceptor");
    }

    public Set<Annotation> getQualifiers() {
        if (parent != null && !isOverridden("qualifiers")) {
            return parent.getQualifiers();
        } else {
            return qualifiers;
        }
    }

    public void setQualifiers(Annotation... qualifiers) {
        setQualifiers(new HashSet<>(Arrays.asList(qualifiers)));
    }

    public void setQualifiers(Set<Annotation> qualifiers) {
        if (stereotypes == null) {
            throw new IllegalArgumentException("Qualifiers list can't be null");
        }
        this.qualifiers = qualifiers;
        setOverridden("qualifiers");
    }

    public Set<Annotation> getStereotypes() {
        if (parent != null && !isOverridden("stereotypes")) {
            return parent.getStereotypes();
        } else {
            return stereotypes;
        }
    }

    public void setStereotypes(Set<Annotation> stereotypes) {
        if (stereotypes == null) {
            throw new IllegalArgumentException("Stereotypes list can't be null");
        }
        this.stereotypes = stereotypes;
        setOverridden("stereotypes");
    }

    public Set<Annotation> getInterceptorBindings() {
        if (parent != null && !isOverridden("interceptorBindings")) {
            return parent.getInterceptorBindings();
        } else {
            return interceptorBindings;
        }
    }

    public void setInterceptorBindings(Set<Annotation> bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("Bindings list can't be null");
        }
        this.interceptorBindings = bindings;
        setOverridden("interceptorBindings");
    }

    private void setOverridden(String property) {
        this.overridden.add(property);
    }

    private boolean isOverridden(String property) {
        return this.overridden.contains(property);
    }
}
