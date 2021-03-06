package ahodanenok.di.value;

import ahodanenok.di.scope.ScopeIdentifier;

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
public final class InstanceValue<T> extends AbstractValue<T> {

    private final Class<? extends T> instanceClass;
    private final Provider<? extends T> provider;

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> InstanceValue(V instance) {
        this((Class<T>) instance.getClass(), instance);
    }

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> InstanceValue(Class<T> clazz, V instance) {
        super(clazz);
        metadata().setScope(ScopeIdentifier.SINGLETON);

        this.instanceClass = (Class<? extends T>) instance.getClass();
        this.provider = () -> instance;
    }

    @Override
    public Class<? extends T> realType() {
        return instanceClass;
    }

    @Override
    public Provider<? extends T> provider() {
        return provider;
    }
}
