package ahodanenok.di.scope;

import ahodanenok.di.exception.ScopeResolutionException;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves element scope by inspecting its annotations.
 * Annotation marked with meta-annotation {@link Scope} is the scope of an element.
 */
public class AnnotatedScopeResolution implements ScopeResolution{

    @Override
    public ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope) {

        ScopeIdentifier scope = null;
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Set<Annotation> scopes = collect(currentClass.getDeclaredAnnotations());
            if (scopes.size() > 1) {
                // todo: error message
                throw new ScopeResolutionException(clazz, "more than 1 scope, found scopes are " + scopes + " declared on the class " + currentClass.getName());
            }

            if (scopes.size() == 1) {
                Annotation scopeAnnotation = scopes.iterator().next();
                if (currentClass == clazz || scopeAnnotation.annotationType().isAnnotationPresent(Inherited.class)) {
                    scope = ScopeIdentifier.of(scopeAnnotation);
                }

                // whether annotation inherited or not, stop
                // if inherited then use it, otherwise don't go up the hierarchy
                break;
            }

            currentClass = currentClass.getSuperclass();
        }

        // todo: is scope is null - check stereotypes
        if (scope != null) {
            return scope;
        } else {
            return defaultScope;
        }
    }

    @Override
    public ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(method.getDeclaredAnnotations());
        if (scopes.size() > 1) {
            // todo: error message
            throw new ScopeResolutionException(method, "more than 1 scope, found scopes are " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            return defaultScope;
        }
    }

    private Set<Annotation> collect(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class))
                .collect(Collectors.toSet());
    }
}
