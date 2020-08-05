package ahodanenok.di;

import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.scope.NotScoped;
import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Supplier;

public class DependencyFieldProviderValue<T> extends AbstractDependencyValue<T> {

    private DIContainerContext context;
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
    public void bind(DIContainerContext context) {
        this.context = context;

        if (id == null) {
            Set<Annotation> qualifiers = context.getQualifierResolution().resolve(field);
            id = DependencyIdentifier.of(type, qualifiers);
        }

        Supplier<Set<Annotation>> stereotypes = () -> context.getStereotypeResolution().resolve(field);

        scope = context.getScopeResolution().resolve(field, stereotypes, ScopeIdentifier.of(NotScoped.class));

        if (name == null) {
            setName(context.getNameResolution().resolve(field, stereotypes));
        }

        if (initOnStartup == null && field.isAnnotationPresent(Eager.class)) {
            setInitOnStartup(true);
        }

        if (defaultValue == null && field.isAnnotationPresent(DefaultValue.class)) {
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
                Object instance = context.getContainer().instance(field.getDeclaringClass());
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
