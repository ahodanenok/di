package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Provides user-supplied value as a dependency.
 *
 * Scope of the value will be singleton.
 * Qualifiers on the instance class are ignored, but the could be provided via
 * {@link ahodanenok.di.DependencyInstanceValue#DependencyInstanceValue(ahodanenok.di.DependencyIdentifier, java.lang.Object)}
 * constructor.
 *
 * @param <T> type of the value
 */
public class DependencyInstanceValue<T> extends AbstractDependencyValue<T> {

    private DependencyIdentifier<T> id;
    private Provider<? extends T> instance;
    private ScopeIdentifier scope;

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> DependencyInstanceValue(V instance) {
        this(instance != null ? DependencyIdentifier.of((Class<T>) instance.getClass()) : null, instance);
    }

    @SuppressWarnings("unchecked") // instance is a subclass of T
    public <V extends T> DependencyInstanceValue(T clazz, V instance) {
        this(instance != null ? DependencyIdentifier.of((Class<T>) clazz) : null, instance);
    }

    public <V extends T> DependencyInstanceValue(DependencyIdentifier<T> id, V instance) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }

        this.id = id;
        this.scope = ScopeIdentifier.of(Singleton.class);
        this.instance = () -> instance;
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public Provider<? extends T> provider() {
        return instance;
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }
}
