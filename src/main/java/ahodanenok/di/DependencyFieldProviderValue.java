package ahodanenok.di;

import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class DependencyFieldProviderValue<T> extends AbstractDependencyValue<T> {

    private DIContainer container;
    private Field field;
    private Class<T> type;
    private DependencyIdentifier<T> id;
    private ScopeIdentifier scope;

    // todo: check field's type is assignable to type

    public DependencyFieldProviderValue(Class<T> type, Field field) {
        this.type = type;
        this.field = field;
        ReflectionAssistant.checkFieldTypeAssignable(type, field);
    }

    public DependencyFieldProviderValue(DependencyIdentifier<T> id, Field field) {
        this.id = id;
        this.field = field;
        ReflectionAssistant.checkFieldTypeAssignable(id.type(), field);
    }

    @Override
    public void bind(DIContainer container) {
        this.container = container;

        if (id == null) {
            Set<Annotation> qualifiers = container.qualifierResolution().resolve(field);
            id = DependencyIdentifier.of(type, qualifiers);
        }

        scope = container.scopeResolution().resolve(field);

        if (field.isAnnotationPresent(DefaultValue.class)) {
            setDefault(true);
        }
    }

    @Override
    public DependencyIdentifier<T> id() {
        return id;
    }

    @Override
    public ScopeIdentifier scope() {
        return scope;
    }

    @Override
    public Provider<? extends T> provider() {
        // todo: errors
        // todo: suppress unchecked
        if (Modifier.isStatic(field.getModifiers())) {
            return () -> {
                try {
                    return (T) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        } else {
            return () -> {
                Object instance = container.instance(field.getDeclaringClass());
                if (instance == null) {
                    throw new UnsatisfiedDependencyException(DependencyIdentifier.of(field.getDeclaringClass()), "not found");
                }

                try {
                    return (T) field.get(instance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}
