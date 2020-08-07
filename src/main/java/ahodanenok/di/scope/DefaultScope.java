package ahodanenok.di.scope;

import ahodanenok.di.value.Value;

/**
 * @see NotScoped
 */
public class DefaultScope implements Scope {

    @Override
    public ScopeIdentifier id() {
        return ScopeIdentifier.NOT_SCOPED;
    }

    @Override
    public <T> T get(Value<T> value) {
        return value.provider().get();
    }
}
