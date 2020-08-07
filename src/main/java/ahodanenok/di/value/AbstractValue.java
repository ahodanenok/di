package ahodanenok.di.value;

import ahodanenok.di.DependencyIdentifier;
import ahodanenok.di.value.metadata.ValueMetadata;

public abstract class AbstractValue<T> implements Value<T> {

    private DependencyIdentifier<T> id;
    private Class<T> type;
    private ValueMetadata<T> metadata;

    public AbstractValue(Class<T> type, ValueMetadata<T> metadata) {
        this.id = DependencyIdentifier.of(type, metadata.qualifiers());
        this.type = type;
        this.metadata = metadata;
    }

    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public ValueMetadata<T> metadata() {
        return metadata;
    }
}
