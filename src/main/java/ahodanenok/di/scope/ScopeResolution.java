package ahodanenok.di.scope;

import ahodanenok.di.Later;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves element scope by inspecting its annotations.
 * Annotation marked with meta-annotation {@link Scope} is the scope of an element.
 */
public final class ScopeResolution {

    private final Provider<StereotypeResolution> stereotypeResolution;

    @Inject
    public ScopeResolution(@Later Provider<StereotypeResolution> stereotypeResolution) {
        this.stereotypeResolution = stereotypeResolution;
    }

    public ScopeIdentifier resolve(AnnotatedElement element, ScopeIdentifier defaultScope) {
        if (element instanceof Class) {
            return resolveForClass((Class<?>) element, defaultScope);
        } else {
            return resolveForElement(element, defaultScope);
        }
    }

    /**
     * Determine scope of the instances of given class.
     * @param defaultScope returned if scope wasn't resolved
     */
    private ScopeIdentifier resolveForClass(Class<?> clazz, ScopeIdentifier defaultScope) {

        ScopeIdentifier scope = null;
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Set<Annotation> scopes = collect(currentClass.getDeclaredAnnotations());
            if (scopes.size() > 1) {
                throw new ConfigurationException("Multiple scopes are declared for the class " + clazz + ": " + scopes);
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

        if (scope == null) {
            Set<ScopeIdentifier> s = scopesFromStereotypes(stereotypeResolution.get().resolve(clazz));
            if (s.size() > 1) {
                throw new ConfigurationException("Multiple scopes are declared on stereotypes for the class " + clazz + ": " + s);
            }

            if (s.size() == 1) {
                scope = s.iterator().next();
            }
        }

        if (scope != null) {
            return scope;
        } else {
            return defaultScope;
        }
    }

    private ScopeIdentifier resolveForElement(AnnotatedElement element, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(element.getDeclaredAnnotations());
        if (scopes.size() > 1) {
            throw new ConfigurationException("Multiple scopes are declared for the element " + element + ": " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            Set<ScopeIdentifier> s = scopesFromStereotypes(stereotypeResolution.get().resolve(element));
            if (s.size() > 1) {
                throw new ConfigurationException("Multiple scopes are declared on stereotypes for the class " + element + ": " + s);
            }

            if (s.size() == 1) {
                return s.iterator().next();
            }

            return defaultScope;
        }
    }

    private Set<Annotation> collect(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class))
                .collect(Collectors.toSet());
    }

    private Set<ScopeIdentifier> scopesFromStereotypes(Set<Annotation> stereotypes) {
        Set<ScopeIdentifier> scopes = new HashSet<>();
        for (Annotation s : stereotypes) {
            ScopeIdentifier stereotypeScope = resolve(s.annotationType(), null);
            if (stereotypeScope != null) {
                scopes.add(stereotypeScope);
            }
        }

        return scopes;
    }
}
