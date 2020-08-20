package ahodanenok.di.value;

import ahodanenok.di.ValueSpecifier;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.metadata.ExplicitMetadata;

import javax.inject.Provider;

/**
 * Provides user-supplied value as a dependency.
 *
 * Scope of the value will be singleton.
 * Qualifiers on the instance class are ignored, but the could be provided via
 * {@link InstanceValue#InstanceValue(ValueSpecifier, java.lang.Object)}
 * constructor.
 *
 * @param <T> type of the value
 */
public class InstanceValue<T> extends AbstractValue<T> {

    private Provider<? extends T> instance;

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> InstanceValue(V instance) {
        this((Class<T>) instance.getClass(), instance);
    }

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> InstanceValue(Class<T> clazz, V instance) {
        super(clazz, new ExplicitMetadata(instance.getClass()));
        metadata().setScope(ScopeIdentifier.SINGLETON);

        this.instance = () -> instance;
    }

    @Override
    public ExplicitMetadata metadata() {
        return (ExplicitMetadata) super.metadata();
    }

    @Override
    public Provider<? extends T> provider() {
        return instance;
    }
}
