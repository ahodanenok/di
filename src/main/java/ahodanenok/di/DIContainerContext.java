package ahodanenok.di;

import ahodanenok.di.name.NameResolution;
import ahodanenok.di.scope.ScopeResolution;
import ahodanenok.di.stereotype.StereotypeResolution;

public class DIContainerContext {

    DIContainer container;
    ScopeResolution scopeResolution;
    QualifierResolution qualifierResolution;
    NameResolution nameResolution;
    StereotypeResolution stereotypeResolution;

    DIContainerContext(DIContainer container) {
        this.container = container;
    }

    public DIContainer getContainer() {
        return container;
    }

    public ScopeResolution getScopeResolution() {
        return scopeResolution;
    }

    public QualifierResolution getQualifierResolution() {
        return qualifierResolution;
    }

    public NameResolution getNameResolution() {
        return nameResolution;
    }

    public StereotypeResolution getStereotypeResolution() {
        return stereotypeResolution;
    }
}
