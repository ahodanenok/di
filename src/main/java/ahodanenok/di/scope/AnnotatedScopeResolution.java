package ahodanenok.di.scope;

import ahodanenok.di.exception.ScopeResolveException;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedScopeResolution implements ScopeResolution{

    @Override
    public ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(clazz.getDeclaredAnnotations(), defaultScope);
        if (scopes.size() > 1) {
            throw new ScopeResolveException(clazz, "more than 1 scope, found scopes are " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            return defaultScope;
        }
    }

    @Override
    public ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(method.getDeclaredAnnotations(), defaultScope);
        if (scopes.size() > 1) {
            throw new ScopeResolveException(method, "more than 1 scope, found scopes are " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            return defaultScope;
        }
    }

    private Set<Annotation> collect(Annotation[] annotations, ScopeIdentifier defaultScope) {
        return Arrays.stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class))
                .collect(Collectors.toSet());
    }
}
