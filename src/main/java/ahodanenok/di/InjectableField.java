package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class InjectableField implements Injectable<Object> {

    private DIContainer container;
    private Field field;

    public InjectableField(DIContainer container, Field field) {
        this.container = container;
        this.field = field;
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

        Set<Annotation> qualifiers = container.instance(QualifierResolution.class).resolve(field);
        DependencyIdentifier<?> id = DependencyIdentifier.of(field.getType(), qualifiers);
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
