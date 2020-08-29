package ahodanenok.di.value;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.metadata.ExplicitMetadata;
import ahodanenok.di.value.metadata.ResolvableMetadata;
import ahodanenok.di.value.metadata.ValueMetadata;

public abstract class AbstractValue<T> implements Value<T> {

    protected Class<T> type;
    protected ValueMetadata metadata;
    protected DIContainer container;

    public AbstractValue(Class<T> type, ValueMetadata metadata) {
        this.type = type;
        this.metadata = metadata;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public ValueMetadata metadata() {
        return metadata;
    }

    public ExplicitMetadata withExplicitMetadata() {
        if (!(metadata instanceof ExplicitMetadata)) {
            metadata = new ExplicitMetadata(metadata.valueType());
        }

        return (ExplicitMetadata) metadata;
    }

    public void setMetadata(ValueMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;
        if (this.metadata instanceof ResolvableMetadata) {
            ((ResolvableMetadata) this.metadata).resolve(container);
        }
    }
}
