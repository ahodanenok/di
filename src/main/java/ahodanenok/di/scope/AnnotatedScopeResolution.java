package ahodanenok.di.scope;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedScopeResolution implements ScopeResolution{

    @Override
    public ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope) {
        return resolveImpl(clazz.getDeclaredAnnotations(), defaultScope);
    }

    @Override
    public ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope) {
        return resolveImpl(method.getDeclaredAnnotations(), defaultScope);
    }

    private ScopeIdentifier resolveImpl(Annotation[] annotations, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = Arrays.stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class))
                .collect(Collectors.toSet());

        // todo: errors

        if (scopes.size() > 1) {
            throw new RuntimeException("more than 1 scope");
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            return defaultScope;
        }
    }
}
