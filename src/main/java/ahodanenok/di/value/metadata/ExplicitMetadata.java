package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;

public class ExplicitMetadata extends ValueMetadata {

    public ExplicitMetadata(Class<?> type) {
        super(type);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScope(ScopeIdentifier scope) {
        this.scope = scope;
    }

    public void setQualifiers(Annotation... qualifiers) {
        this.qualifiers = new HashSet<>(Arrays.asList(qualifiers));
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
