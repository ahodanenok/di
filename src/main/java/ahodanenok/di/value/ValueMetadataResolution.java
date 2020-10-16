package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.interceptor.InterceptorMetadataResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.qualifier.QualifierResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;
import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.AnnotatedElement;

public final class ValueMetadataResolution {

    private Provider<NameResolution> nameResolution;
    private Provider<StereotypeResolution> stereotypeResolution;
    private Provider<QualifierResolution> qualifierResolution;
    private Provider<ScopeResolution> scopeResolution;
    private Provider<InterceptorMetadataResolution> interceptorMetadataResolution;

    @Inject
    public void setNameResolution(@Later Provider<NameResolution> nameResolution) {
        this.nameResolution = nameResolution;
    }

    @Inject
    public void setStereotypeResolution(@Later Provider<StereotypeResolution> stereotypeResolution) {
        this.stereotypeResolution = stereotypeResolution;
    }

    @Inject
    public void setQualifierResolution(@Later Provider<QualifierResolution> qualifierResolution) {
        this.qualifierResolution = qualifierResolution;
    }

    @Inject
    public void setScopeResolution(@Later Provider<ScopeResolution> scopeResolution) {
        this.scopeResolution = scopeResolution;
    }

    @Inject
    public void setInterceptorMetadataResolution(@Later Provider<InterceptorMetadataResolution> interceptorMetadataResolution) {
        this.interceptorMetadataResolution = interceptorMetadataResolution;
    }

    public MutableValueMetadata resolve(AnnotatedElement element) {
        MutableValueMetadata metadata = new MutableValueMetadata();
        metadata.setName(nameResolution.get().resolve(element));
        metadata.setQualifiers(qualifierResolution.get().resolve(element));
        metadata.setScope(scopeResolution.get().resolve(element, ScopeIdentifier.NOT_SCOPED));
        metadata.setStereotypes(stereotypeResolution.get().resolve(element));
        metadata.setPrimary(element.isAnnotationPresent(Primary.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Primary.class)));
        metadata.setDefault(element.isAnnotationPresent(Default.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Default.class)));

        Eager eagerAnnotation = element.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            metadata.setEager(true);
            metadata.setInitializationPhase(eagerAnnotation.phase());
        }

        Priority p = element.getAnnotation(Priority.class);
        if (p != null) {
            metadata.setPriority(p.value());
        }

        if (element instanceof Class) {
            metadata.setInterceptor(interceptorMetadataResolution.get().isInterceptor((Class<?>) element));
        }
        metadata.setInterceptorBindings(interceptorMetadataResolution.get().resolveBindings(element));

        return metadata;
    }
}
