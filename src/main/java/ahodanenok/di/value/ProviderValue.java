package ahodanenok.di.value;

import ahodanenok.di.DIContainer;

import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.inject.Provider;

public class ProviderValue<T> extends AbstractValue<T> {

    private final Value<? extends Provider<? extends T>> providerValue;

    public <V extends Provider<? extends T>> ProviderValue(Class<T> type, Class<V> providerClass) {
        super(type);
        this.providerValue = new InstantiatingValue<>(providerClass);
    }

    public <V extends Provider<? extends T>> ProviderValue(Class<T> type, V provider) {
        super(type);
        this.providerValue = new InstanceValue<>(provider);
    }

    @Override
    public Class<? extends T> realType() {
        return type();
    }

    @Override
    public void bind(DIContainer container) {
        super.bind(container);
        providerValue.bind(container);
    }

    @Override
    public MutableValueMetadata metadata() {
        return providerValue.metadata();
    }

    @Override
    public Provider<? extends T> provider() {
        return providerValue.provider().get();
    }
}
