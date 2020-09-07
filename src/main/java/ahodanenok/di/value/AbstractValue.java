package ahodanenok.di.value;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.metadata.*;

public abstract class AbstractValue<T> implements Value<T> {

    protected Class<T> type;
    protected MutableValueMetadata metadata;
    protected DIContainer container;
    protected boolean resolveMetadata;

    public AbstractValue(Class<T> type) {
        this.type = type;
        this.metadata = new MutableValueMetadata();
        this.resolveMetadata = true;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public MutableValueMetadata metadata() {
        return metadata;
    }

    public void setResolveMetadata(boolean resolveMetadata) {
        this.resolveMetadata = resolveMetadata;
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;
        if (resolveMetadata) {
            MutableValueMetadata resolved = resolveMetadata();
            if (resolved != null) {
                MutableValueMetadata current = metadata();
                while (current.getParent() != null) {
                    current = current.getParent();
                }

                current.setParent(resolved);
            }
        }
    }

    protected MutableValueMetadata resolveMetadata() {
        return null;
    }
}
