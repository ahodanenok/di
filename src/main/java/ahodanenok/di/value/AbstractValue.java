package ahodanenok.di.value;

import ahodanenok.di.DIContainer;
import ahodanenok.di.DependencyIdentifier;
import ahodanenok.di.value.metadata.ResolvableMetadata;
import ahodanenok.di.value.metadata.ValueMetadata;

public abstract class AbstractValue<T> implements Value<T> {

    protected DependencyIdentifier<T> id;
    protected Class<T> type;
    protected ValueMetadata metadata;
    protected DIContainer container;

    public AbstractValue(Class<T> type, ValueMetadata metadata) {
        this.type = type;
        this.metadata = metadata;
        this.id = DependencyIdentifier.of(type, metadata.getQualifiers());
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public ValueMetadata metadata() {
        return metadata;
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;
        if (this.metadata instanceof ResolvableMetadata) {
            ((ResolvableMetadata) this.metadata).resolve(container);
            this.id = DependencyIdentifier.of(type, metadata.getQualifiers());
        }
    }
}
