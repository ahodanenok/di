package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class UnmodifiableValueMetadata extends MutableValueMetadata {

    private MutableValueMetadata metadata;

    public UnmodifiableValueMetadata(MutableValueMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void overrideWith(MutableValueMetadata metadata) {
        throwNotModifiable();
    }

    @Override
    public String getName() {
        return metadata.getName();
    }

    @Override
    public void setName(String name) {
        throwNotModifiable();
    }

    @Override
    public ScopeIdentifier getScope() {
        return metadata.getScope();
    }

    @Override
    public void setScope(ScopeIdentifier scope) {
        throwNotModifiable();
    }

    @Override
    public String getProfilesCondition() {
        return metadata.getProfilesCondition();
    }

    @Override
    public void setProfilesCondition(String profilesCondition) {
        throwNotModifiable();
    }

    @Override
    public int getInitializationPhase() {
        return metadata.getInitializationPhase();
    }

    @Override
    public void setInitializationPhase(int phase) {
        throwNotModifiable();
    }

    @Override
    public int getPriority() {
        return metadata.getPriority();
    }

    @Override
    public void setPriority(int priority) {
        throwNotModifiable();
    }

    @Override
    public boolean isPrimary() {
        return metadata.isPrimary();
    }

    @Override
    public void setPrimary(boolean isPrimary) {
        throwNotModifiable();
    }

    @Override
    public boolean isDefault() {
        return metadata.isDefault();
    }

    @Override
    public void setDefault(boolean isDefault) {
        throwNotModifiable();
    }

    @Override
    public boolean isEager() {
        return metadata.isEager();
    }

    @Override
    public void setEager(boolean eager) {
        throwNotModifiable();
    }

    @Override
    public boolean isInterceptor() {
        return metadata.isInterceptor();
    }

    @Override
    public void setInterceptor(boolean interceptor) {
        throwNotModifiable();
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return Collections.unmodifiableSet(metadata.getQualifiers());
    }

    @Override
    public void setQualifiers(Annotation... qualifiers) {
        throwNotModifiable();
    }

    @Override
    public void setQualifiers(Set<Annotation> qualifiers) {
        throwNotModifiable();
    }

    @Override
    public Set<Annotation> getStereotypes() {
        return Collections.unmodifiableSet(metadata.getStereotypes());
    }

    @Override
    public void setStereotypes(Set<Annotation> stereotypes) {
        throwNotModifiable();
    }

    @Override
    public Set<Annotation> getInterceptorBindings() {
        return Collections.unmodifiableSet(metadata.getInterceptorBindings());
    }

    @Override
    public void setInterceptorBindings(Set<Annotation> bindings) {
        throwNotModifiable();
    }

    private void throwNotModifiable() {
        throw new IllegalStateException("Metadata is not modifiable");
    }
}
