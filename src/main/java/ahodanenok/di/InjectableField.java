package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class InjectableField implements Injectable<Object> {

    private DIContainer container;
    private Field field;
    private QualifierResolution qualifierResolution;

    public InjectableField(DIContainer container, Field field) {
        this.container = container;
        this.field = field;
        // todo: get QualifierResolution from container
        this.qualifierResolution = new AnnotatedQualifierResolution();
    }

    @Override
    public Object inject(Object instance) {
        // todo: handle generics
        // todo: set accessible

        // todo: conform to spec
        // Injectable fields:
        // are annotated with @Inject.
        // are not final.
        // may have any otherwise valid name.

        if (Modifier.isFinal(field.getModifiers())) {
            throw new InjectionFailedException(field, "field is final");
        }

        Annotation qualifier = qualifierResolution.resolve(field);
        DependencyIdentifier<?> id = DependencyIdentifier.of(field.getType(), qualifier);
        Object value = container.instance(id);
        if (value == null && !field.isAnnotationPresent(OptionalDependency.class)) {
            throw new UnsatisfiedDependencyException(this, id, "not found");
        }

        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new InjectionFailedException(field, e);
        } finally {
            field.setAccessible(accessible);
        }

        return instance;
    }

    @Override
    public String toString() {
        return "injectable(" + field.toGenericString() + ")";
    }
}
