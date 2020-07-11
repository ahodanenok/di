package ahodanenok.di.scope;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedScopeResolution implements ScopeResolution{

    @Override
    public ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = Arrays.stream(clazz.getDeclaredAnnotations())
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
