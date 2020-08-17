package ahodanenok.di;

import ahodanenok.di.exception.InjectionFailedException;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.interceptor.AroundInject;
import ahodanenok.di.interceptor.InjectionPointImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Consumer;

public class InjectableField implements Injectable<Object> {

    private DIContainer container;
    private Field field;
    private Consumer<AroundInject> onInject;

    public InjectableField(DIContainer container, Field field) {
        this.container = container;
        this.field = field;
    }

    public void setOnInject(Consumer<AroundInject> onInject) {
        this.onInject = onInject;
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

        if (onInject != null) {
            InjectionPointImpl injectionPoint = new InjectionPointImpl();
            injectionPoint.setType(field.getType());
            injectionPoint.setQualifiers(qualifiers);
            injectionPoint.setTarget(field);
            onInject.accept(new AroundInject(injectionPoint, value -> doInject(instance, value)));
        } else {
            DependencyIdentifier<?> id = DependencyIdentifier.of(field.getType(), qualifiers);
            Object value = container.instance(id);
            if (value == null && !field.isAnnotationPresent(OptionalDependency.class)) {
                throw new UnsatisfiedDependencyException(this, id, "not found");
            }

            doInject(instance, value);
        }

        return instance;
    }

    private void doInject(Object instance, Object value) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new InjectionFailedException(field, e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    @Override
    public String toString() {
        return "injectable(" + field.toGenericString() + ")";
    }
}
