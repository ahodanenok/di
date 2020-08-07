package ahodanenok.di.scope;

import javax.inject.Scope;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;

// constants for singleton, not scoped
public final class ScopeIdentifier {

    public static final ScopeIdentifier NOT_SCOPED = of(NotScoped.class);
    public static final ScopeIdentifier SINGLETON = of(Singleton.class);

    public static ScopeIdentifier of(Annotation annotation) {
        return of(annotation.annotationType());
    }

    public static ScopeIdentifier of(Class<?> annotationClass) {

        if (annotationClass == null) {
            throw new IllegalArgumentException("Annotation class is null");
        }

        if (!annotationClass.isAnnotation()) {
            throw new IllegalArgumentException(String.format("Class '%s' is not annotation", annotationClass.getName()));
        }

        if (!annotationClass.isAnnotationPresent(Scope.class)) {
            throw new IllegalArgumentException(String.format("Class '%s' is not marked with @Scope annotation", annotationClass.getName()));
        }

        return new ScopeIdentifier(annotationClass.getName());
    }

    private String name;

    private ScopeIdentifier(String name) {
        this.name = name;
    }

    public String get() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("ScopeID(%s)", get());
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (this == object) {
            return true;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        ScopeIdentifier other = (ScopeIdentifier) object;
        return name.equals(other.name);
    }
}
