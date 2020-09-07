package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ValueMetadata {

    String getName();

    ScopeIdentifier getScope();

    String getProfilesCondition();

    int getInitializationPhase();

    int getPriority();

    boolean isPrimary();

    boolean isDefault();

    boolean isEager();

    boolean isInterceptor();

    Set<Annotation> getQualifiers();

    Set<Annotation> getStereotypes();

    Set<Annotation> getInterceptorBindings();
}
