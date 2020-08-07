package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.value.metadata.FieldMetadata;

import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldProviderValue<T> extends AbstractValue<T> {

    private DIContainerContext context;
    private Field field;

    // todo: check field's type is assignable to type

    public FieldProviderValue(Class<T> type, Field field) {
        super(type, new FieldMetadata<>(field));
        this.field = field;
        ReflectionAssistant.checkFieldTypeAssignable(type, field);
    }

    @Override
    public void bind(DIContainerContext context) {
        this.context = context;

//        if (id == null) {
//            Set<Annotation> qualifiers = context.getQualifierResolution().resolve(field);
//            id = DependencyIdentifier.of(type, qualifiers);
//        }
//
//        Supplier<Set<Annotation>> stereotypes = () -> context.getStereotypeResolution().resolve(field);
//
//        scope = context.getScopeResolution().resolve(field, stereotypes, ScopeIdentifier.of(NotScoped.class));
//
//        if (name == null) {
//            setName(context.getNameResolution().resolve(field, stereotypes));
//        }
//
//        if (initOnStartup == null && field.isAnnotationPresent(Eager.class)) {
//            setInitOnStartup(true);
//        }
//
//        if (defaultValue == null && field.isAnnotationPresent(DefaultValue.class)) {
//            setDefault(true);
//        }
    }
//
//    @Override
//    public DependencyIdentifier<T> id() {
//        return id;
//    }
//
//    @Override
//    public ScopeIdentifier scope() {
//        return scope;
//    }

    @Override
    public Provider<? extends T> provider() {
        // todo: errors
        // todo: suppress unchecked
        if (Modifier.isStatic(field.getModifiers())) {
            return () -> {
                boolean accessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    return (T) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    field.setAccessible(accessible);
                }
            };
        } else {
            return () -> {
                Object instance = context.getContainer().instance(field.getDeclaringClass());
                if (instance == null) {
                    throw new UnsatisfiedDependencyException(DependencyIdentifier.of(field.getDeclaringClass()), "not found");
                }

                try {
                    boolean accessible = field.isAccessible();
                    try {
                        field.setAccessible(true);
                        return (T) field.get(instance);
                    } finally {
                        field.setAccessible(accessible);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}
