package ahodanenok.di.value.metadata;

import ahodanenok.di.*;
import ahodanenok.di.interceptor.InterceptorMetadataResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import java.lang.reflect.Field;
import java.util.Collections;

public class FieldMetadata<T> extends ValueMetadata implements ResolvableMetadata {

    private final Field field;

    public FieldMetadata(Class<T> type, Field field) {
        super(type);
        this.field = field;
    }

    @Override
    public void resolve(DIContainer container) {
        stereotypes = container.instance(StereotypeResolution.class).resolve(field);
        name = container.instance(NameResolution.class).resolve(field);
        qualifiers = container.instance(QualifierResolution.class).resolve(field);
        scope = container.instance(ScopeResolution.class).resolve(field, ScopeIdentifier.NOT_SCOPED);
        isPrimary = field.isAnnotationPresent(PrimaryValue.class)
                || stereotypes.stream().anyMatch(s -> s.annotationType().isAnnotationPresent(PrimaryValue.class));
        isDefault = field.isAnnotationPresent(DefaultValue.class)
                || stereotypes.stream().anyMatch(s -> s.annotationType().isAnnotationPresent(DefaultValue.class));

        Eager eagerAnnotation = field.getAnnotation(Eager.class);
        if (eagerAnnotation != null) {
            eager = true;
            initializationPhase = eagerAnnotation.phase();
        }

        InterceptorMetadataResolution interceptorMetadataResolution =
                container.instance(InterceptorMetadataResolution.class);
        interceptorBindings = interceptorMetadataResolution.resolveBindings(field);
    }
}
