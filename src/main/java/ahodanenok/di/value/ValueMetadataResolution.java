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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public MutableValueMetadata resolve(Class<?> clazz) {

        MutableValueMetadata metadata = new MutableValueMetadata();
        metadata.setName(nameResolution.get().resolve(clazz));
        metadata.setQualifiers(qualifierResolution.get().resolve(clazz));
        metadata.setScope(scopeResolution.get().resolve(clazz, ScopeIdentifier.NOT_SCOPED));
        metadata.setStereotypes(stereotypeResolution.get().resolve(clazz));
        metadata.setPrimary(clazz.isAnnotationPresent(Primary.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Primary.class)));
        metadata.setDefault(clazz.isAnnotationPresent(Default.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Default.class)));

        Eager eagerAnnotation = clazz.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            metadata.setEager(true);
            metadata.setInitializationPhase(eagerAnnotation.phase());
        }

        Priority p = clazz.getAnnotation(Priority.class);
        if (p != null) {
            metadata.setPriority(p.value());
        }

        metadata.setInterceptor(interceptorMetadataResolution.get().isInterceptor(clazz));
        metadata.setInterceptorBindings(interceptorMetadataResolution.get().resolveBindings(clazz));

        return metadata;
    }

    public MutableValueMetadata resolve(Method method) {
        MutableValueMetadata metadata = new MutableValueMetadata();
        metadata.setName(nameResolution.get().resolve(method));
        metadata.setQualifiers(qualifierResolution.get().resolve(method));
        metadata.setScope(scopeResolution.get().resolve(method, ScopeIdentifier.NOT_SCOPED));
        metadata.setStereotypes(stereotypeResolution.get().resolve(method));
        metadata.setPrimary(method.isAnnotationPresent(Primary.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Primary.class)));
        metadata.setDefault(method.isAnnotationPresent(Default.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Default.class)));

        Eager eagerAnnotation = method.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            metadata.setEager(true);
            metadata.setInitializationPhase(eagerAnnotation.phase());
        }

        Priority p = method.getAnnotation(Priority.class);
        if (p != null) {
            metadata.setPriority(p.value());
        }

        metadata.setInterceptorBindings(interceptorMetadataResolution.get().resolveBindings(method));

        return metadata;
    }

    public MutableValueMetadata resolve(Field field) {
        MutableValueMetadata metadata = new MutableValueMetadata();
        metadata.setName(nameResolution.get().resolve(field));
        metadata.setQualifiers(qualifierResolution.get().resolve(field));
        metadata.setScope(scopeResolution.get().resolve(field, ScopeIdentifier.NOT_SCOPED));
        metadata.setStereotypes(stereotypeResolution.get().resolve(field));
        metadata.setPrimary(field.isAnnotationPresent(Primary.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Primary.class)));
        metadata.setDefault(field.isAnnotationPresent(Default.class)
                || metadata.getStereotypes().stream().anyMatch(s -> s.annotationType().isAnnotationPresent(Default.class)));

        Eager eagerAnnotation = field.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            metadata.setEager(true);
            metadata.setInitializationPhase(eagerAnnotation.phase());
        }

        metadata.setInterceptorBindings(interceptorMetadataResolution.get().resolveBindings(field));

        return metadata;
    }
}
