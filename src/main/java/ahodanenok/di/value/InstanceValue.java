package ahodanenok.di.value;

import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.metadata.ClassMetadata;
import ahodanenok.di.value.metadata.ExplicitMetadata;
import ahodanenok.di.value.metadata.ValueMetadata;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Provides user-supplied value as a dependency.
 *
 * Scope of the value will be singleton.
 * Qualifiers on the instance class are ignored, but the could be provided via
 * {@link InstanceValue#InstanceValue(ahodanenok.di.DependencyIdentifier, java.lang.Object)}
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
        super(clazz, new ExplicitMetadata<>((Class<T>) instance.getClass()));

        metadata().setScope(ScopeIdentifier.SINGLETON);
//
//        if (id == null) {
//            throw new IllegalArgumentException("id is null");
//        }

        this.instance = () -> instance;
    }

    @Override
    public ExplicitMetadata<T> metadata() {
        return (ExplicitMetadata<T>) super.metadata();
    }

    @Override
    public Provider<? extends T> provider() {
        return instance;
    }
}
