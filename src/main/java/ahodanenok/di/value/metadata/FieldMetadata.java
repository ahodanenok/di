package ahodanenok.di.value.metadata;

import ahodanenok.di.*;
import ahodanenok.di.name.NameResolution;
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
        name = container.instance(NameResolution.class).resolve(field, this::getStereotypes);
        qualifiers = container.instance(QualifierResolution.class).resolve(field);
        scope = container.instance(ScopeResolution.class).resolve(field);
        isDefault = field.isAnnotationPresent(DefaultValue.class);
        eager = field.isAnnotationPresent(Eager.class);

        // todo: interceptorBindings
        interceptorBindings = Collections.emptySet();
    }
}