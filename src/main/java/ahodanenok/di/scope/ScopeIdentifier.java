package ahodanenok.di.scope;

import javax.inject.Scope;

public class ScopeIdentifier {

    private Class<?> annotationClass;

    public ScopeIdentifier(Class<?> annotationClass) {
        if (annotationClass == null) {
            throw new IllegalArgumentException("Annotation class is null");
        }

        if (!annotationClass.isAnnotation()) {
            throw new IllegalArgumentException(String.format("Class '%s' is not annotation", annotationClass.getName()));
        }

        if (!annotationClass.isAnnotationPresent(Scope.class)) {
            throw new IllegalArgumentException(String.format("Class '%s' is not marked with @Scope annotation", annotationClass.getName()));
        }

        this.annotationClass = annotationClass;
    }

    public String get() {
        return annotationClass.getName();
    }
}