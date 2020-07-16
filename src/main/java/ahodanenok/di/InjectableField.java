package ahodanenok.di;

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
            throw new RuntimeException("can't set final field");
        }

        Annotation qualifier = qualifierResolution.resolve(field);
        DependencyIdentifier<?> id = DependencyIdentifier.of(field.getType(), qualifier);
        Object value = container.instance(id);
        if (value == null && !field.isAnnotationPresent(OptionalDependency.class)) {
            throw new RuntimeException("no instance for " + id);
        }

        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            // todo: errors
            throw new RuntimeException(e);
        }

        return instance;
    }
}
