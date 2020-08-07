package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class ExplicitMetadata<T> implements ValueMetadata<T> {

    private Class<? extends T> type;
    private String name;
    private ScopeIdentifier scope;
    private boolean isDefault;
    private boolean eager;

    public ExplicitMetadata(Class<T> type) {
        this.type = type;
    }

    @Override
    public Class<? extends T> valueType() {
        return type;
    }

    @Override
    public String name() {
       return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }

    public void setScope(ScopeIdentifier scope) {
        this.scope = scope;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isEager() {
        return eager;
    }

    public void setEager(boolean eager) {
        this.eager = eager;
    }

    @Override
    public Set<Annotation> qualifiers() {
        return Collections.emptySet();
    }

    @Override
    public Set<Annotation> stereotypes() {
        return Collections.emptySet();
    }
}
