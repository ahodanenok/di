package ahodanenok.di.value;

import ahodanenok.di.DIContainer;
import ahodanenok.di.value.metadata.ClassMetadata;
import ahodanenok.di.value.metadata.ValueMetadata;

import javax.inject.Provider;

public class ProviderValue<T> extends AbstractValue<T> {

    private Value<? extends Provider<? extends T>> providerValue;

    public <V extends Provider<? extends T>> ProviderValue(Class<T> type, Class<V> providerClass) {
        super(type, new ClassMetadata(providerClass));
        this.providerValue = new InstantiatingValue<>(providerClass);
    }

    public <V extends Provider<? extends T>> ProviderValue(Class<T> type, V provider) {
        super(type, new ClassMetadata(provider.getClass()));
        this.providerValue = new InstanceValue<>(provider);
    }

    @Override
    public void bind(DIContainer container) {
        super.bind(container);
        providerValue.bind(container);
    }

    @Override
    public ValueMetadata metadata() {
        return providerValue.metadata();
    }

    @Override
    public Provider<? extends T> provider() {
        return providerValue.provider().get();
    }
}
