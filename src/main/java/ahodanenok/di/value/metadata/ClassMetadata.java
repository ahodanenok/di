package ahodanenok.di.value.metadata;

import ahodanenok.di.*;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class ClassMetadata<T> implements ValueMetadata<T> {

    private Class<? extends T> type;
    private QualifierResolution qualifierResolution;
    private ScopeResolution scopeResolution;
    private StereotypeResolution stereotypeResolution;
    private NameResolution nameResolution;

    public ClassMetadata(Class<? extends T> type) {
        this.type = type;
        this.qualifierResolution = new AnnotatedQualifierResolution();
        this.scopeResolution = new AnnotatedScopeResolution();
        this.stereotypeResolution = new AnnotatedStereotypeResolution();
        this.nameResolution = new AnnotatedNameResolution();
    }

    @Override
    public Class<? extends T> valueType() {
        return type;
    }

    @Override
    public String name() {
        // todo: cache
        return nameResolution.resolve(type, this::stereotypes);
    }

    @Override
    public ScopeIdentifier scope() {
        // todo: cache
        return scopeResolution.resolve(type, this::stereotypes, ScopeIdentifier.NOT_SCOPED);
    }

    @Override
    public boolean isDefault() {
        // todo: cache
        return type.isAnnotationPresent(DefaultValue.class);
    }

    @Override
    public boolean isEager() {
        // todo: cache
        return type.isAnnotationPresent(Eager.class);
    }

    @Override
    public Set<Annotation> qualifiers() {
        // todo: cache
        return qualifierResolution.resolve(type);
    }

    @Override
    public Set<Annotation> stereotypes() {
        // todo: cache
        return stereotypeResolution.resolve(type);
    }
}
