package ahodanenok.di.value.metadata;

import ahodanenok.di.scope.ScopeIdentifier;

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

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
