package ahodanenok.di.scope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Determines scope of the given element.
 */
public interface ScopeResolution {

    default ScopeIdentifier resolve(Class<?> clazz) {
        return resolve(clazz, Collections::emptySet, ScopeIdentifier.of(NotScoped.class));
    }

    /**
     * Determine scope of the instances of given class.
     * @param defaultScope returned if scope wasn't resolved
     */
    ScopeIdentifier resolve(Class<?> clazz, Supplier<Set<Annotation>> stereotypes, ScopeIdentifier defaultScope);

    default ScopeIdentifier resolve(Method method) {
        return resolve(method, Collections::emptySet, ScopeIdentifier.of(NotScoped.class));
    }

    /**
     * Determine scope of the instances created by given method
     * @param defaultScope returned if scope wasn't resolved
     */
    ScopeIdentifier resolve(Method method, Supplier<Set<Annotation>> stereotypes, ScopeIdentifier defaultScope);

    default ScopeIdentifier resolve(Field field) {
        return resolve(field, Collections::emptySet, ScopeIdentifier.of(NotScoped.class));
    }

    ScopeIdentifier resolve(Field field, Supplier<Set<Annotation>> stereotypes, ScopeIdentifier defaultScope);
}
