package ahodanenok.di.scope;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Determines scope of the given element.
 */
public interface ScopeResolution {

    default ScopeIdentifier resolve(Class<?> clazz) {
        return resolve(clazz, ScopeIdentifier.of(NotScoped.class));
    }

    /**
     * Determine scope of the instances of given class.
     * @param defaultScope returned if scope wasn't resolved
     */
    ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope);

    default ScopeIdentifier resolve(Method method) {
        return resolve(method, ScopeIdentifier.of(NotScoped.class));
    }

    /**
     * Determine scope of the instances created by given method
     * @param defaultScope returned if scope wasn't resolved
     */
    ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope);

    default ScopeIdentifier resolve(Field field) {
        return resolve(field, ScopeIdentifier.of(NotScoped.class));
    }

    ScopeIdentifier resolve(Field field, ScopeIdentifier defaultScope);
}
