package ahodanenok.di.value.metadata;

import ahodanenok.di.AnnotatedQualifierResolution;
import ahodanenok.di.DefaultValue;
import ahodanenok.di.Eager;
import ahodanenok.di.QualifierResolution;
import ahodanenok.di.name.AnnotatedNameResolution;
import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.AnnotatedScopeResolution;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.AnnotatedStereotypeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class MethodMetadata<T> implements ValueMetadata<T> {

    private Method method;
    private QualifierResolution qualifierResolution;
    private ScopeResolution scopeResolution;
    private StereotypeResolution stereotypeResolution;
    private NameResolution nameResolution;

    // todo: pass type explicitly, method return type may not be a real type
    public MethodMetadata(Method method) {
        this.method = method;
        this.qualifierResolution = new AnnotatedQualifierResolution();
        this.scopeResolution = new AnnotatedScopeResolution();
        this.stereotypeResolution = new AnnotatedStereotypeResolution();
        this.nameResolution = new AnnotatedNameResolution();
    }

    @Override
    public Class<? extends T> valueType() {
        return (Class<? extends T>) method.getReturnType();
    }

    @Override
    public String name() {
        return nameResolution.resolve(method, this::stereotypes);
    }

    @Override
    public ScopeIdentifier scope() {
        return scopeResolution.resolve(method);
    }

    @Override
    public boolean isDefault() {
        return method.isAnnotationPresent(DefaultValue.class);
    }

    @Override
    public boolean isEager() {
        return method.isAnnotationPresent(Eager.class);
    }

    @Override
    public Set<Annotation> qualifiers() {
        return qualifierResolution.resolve(method);
    }

    @Override
    public Set<Annotation> stereotypes() {
        return stereotypeResolution.resolve(method);
    }
}
