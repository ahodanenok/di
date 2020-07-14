package ahodanenok.di.scope;

import java.lang.reflect.Method;

// todo: allow to use custom implementations
public interface ScopeResolution {

    default ScopeIdentifier resolve(Class<?> clazz) {
        return resolve(clazz, ScopeIdentifier.of(NotScoped.class));
    }

    ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope);

    default ScopeIdentifier resolve(Method method) {
        return resolve(method, ScopeIdentifier.of(NotScoped.class));
    }

    ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope);
}
