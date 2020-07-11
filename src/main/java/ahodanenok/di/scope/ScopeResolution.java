package ahodanenok.di.scope;

// todo: allow to use custom implementations
public interface ScopeResolution {

    default ScopeIdentifier resolve(Class<?> clazz) {
        return resolve(clazz, null);
    }

    ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope);
}
