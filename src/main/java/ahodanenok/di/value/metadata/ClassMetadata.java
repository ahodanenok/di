package ahodanenok.di.value.metadata;

import ahodanenok.di.*;
import ahodanenok.di.interceptor.InterceptorMetadataResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.annotation.Priority;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public final class ClassMetadata extends ValueMetadata implements ResolvableMetadata {

    public ClassMetadata(Class<?> type) {
        super(type);
    }

    @Override
    public void resolve(DIContainer container) {
        stereotypes = container.instance(StereotypeResolution.class).resolve(type);
        name = container.instance(NameResolution.class).resolve(type);
        qualifiers = container.instance(QualifierResolution.class).resolve(type);
        scope = container.instance(ScopeResolution.class).resolve(type, ScopeIdentifier.NOT_SCOPED);
        isPrimary = type.isAnnotationPresent(PrimaryValue.class)
                || stereotypes.stream().anyMatch(s -> s.annotationType().isAnnotationPresent(PrimaryValue.class));
        isDefault = type.isAnnotationPresent(DefaultValue.class)
                || stereotypes.stream().anyMatch(s -> s.annotationType().isAnnotationPresent(DefaultValue.class));

        Eager eagerAnnotation = type.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            eager = true;
            initializationPhase = eagerAnnotation.phase();
        }

        Priority p = type.getAnnotation(Priority.class);
        if (p != null) {
            priority = p.value();
        }

        InterceptorMetadataResolution interceptorMetadataResolution =
                container.instance(InterceptorMetadataResolution.class);
        interceptor = interceptorMetadataResolution.isInterceptor(type);
        if (interceptor) {
            interceptorBindings = interceptorMetadataResolution.resolveBindings(type);
        }
    }
}
