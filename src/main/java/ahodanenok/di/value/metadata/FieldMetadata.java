package ahodanenok.di.value.metadata;

import ahodanenok.di.AnnotatedQualifierResolution;
import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;
import ahodanenok.di.QualifierResolution;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public class FieldMetadata<T> implements ValueMetadata<T> {

    private Field field;
    private QualifierResolution qualifierResolution;
    private ScopeResolution scopeResolution;
    private StereotypeResolution stereotypeResolution;
    private NameResolution nameResolution;

    // todo: pass type explicitly, field type may not be a real type
    public FieldMetadata(Field field) {
        this.field = field;
        this.qualifierResolution = new AnnotatedQualifierResolution();
        this.scopeResolution = new AnnotatedScopeResolution();
        this.stereotypeResolution = new AnnotatedStereotypeResolution();
        this.nameResolution = new AnnotatedNameResolution();
    }

    @Override
    public Class<? extends T> valueType() {
        return (Class<? extends T>) field.getType();
    }

    @Override
    public String name() {
        return nameResolution.resolve(field, this::stereotypes);
    }

    @Override
    public ScopeIdentifier scope() {
        return scopeResolution.resolve(field);
    }

    @Override
    public boolean isDefault() {
        return field.isAnnotationPresent(DefaultValue.class);
    }

    @Override
    public boolean isEager() {
        return field.isAnnotationPresent(Eager.class);
    }

    @Override
    public Set<Annotation> qualifiers() {
        return qualifierResolution.resolve(field);
    }

    @Override
    public Set<Annotation> stereotypes() {
        return stereotypeResolution.resolve(field);
    }
}
