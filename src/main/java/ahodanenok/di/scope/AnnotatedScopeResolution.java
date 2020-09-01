package ahodanenok.di.scope;

import ahodanenok.di.DIContainer;
import ahodanenok.di.Later;
import ahodanenok.di.exception.ConfigurationException;
import ahodanenok.di.exception.ScopeResolutionException;
import ahodanenok.di.stereotype.StereotypeResolution;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves element scope by inspecting its annotations.
 * Annotation marked with meta-annotation {@link Scope} is the scope of an element.
 */
public class AnnotatedScopeResolution implements ScopeResolution {

    private DIContainer container;
    private Provider<StereotypeResolution> stereotypeResolution;

    @Inject
    public AnnotatedScopeResolution(DIContainer container, @Later Provider<StereotypeResolution> stereotypeResolution) {
        this.container = container;
        this.stereotypeResolution = stereotypeResolution;
    }

    @Override
    public ScopeIdentifier resolve(Class<?> clazz, ScopeIdentifier defaultScope) {

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

    @Override
    public ScopeIdentifier resolve(Method method, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(method.getDeclaredAnnotations());
        if (scopes.size() > 1) {
            throw new ConfigurationException("Multiple scopes are declared for the method " + method + ": " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            Set<ScopeIdentifier> s = scopesFromStereotypes(stereotypeResolution.get().resolve(method));
            if (s.size() > 1) {
                throw new ConfigurationException("Multiple scopes are declared on stereotypes for the class " + method + ": " + s);
            }

            if (s.size() == 1) {
                return s.iterator().next();
            }


            return defaultScope;
        }
    }

    @Override
    public ScopeIdentifier resolve(Field field, ScopeIdentifier defaultScope) {
        Set<Annotation> scopes = collect(field.getDeclaredAnnotations());
        if (scopes.size() > 1) {
            throw new ConfigurationException("Multiple scopes are declared for the field " + field + ": " + scopes);
        }

        if (scopes.size() == 1) {
            return ScopeIdentifier.of(scopes.iterator().next());
        } else {
            Set<ScopeIdentifier> s = scopesFromStereotypes(stereotypeResolution.get().resolve(field));
            if (s.size() > 1) {
                throw new ConfigurationException("Multiple scopes are declared on stereotypes for the class " + field + ": " + s);
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
