package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.value.metadata.FieldMetadata;

import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldProviderValue<T> extends AbstractValue<T> {

    private Field field;

    // todo: check field's type is assignable to type

    public FieldProviderValue(Class<T> type, Field field) {
        super(type, new FieldMetadata<>(type, field));
        this.field = field;
        ReflectionAssistant.checkFieldTypeAssignable(type, field);
    }

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
                Object instance = container.instance(field.getDeclaringClass());
                if (instance == null) {
                    throw new UnsatisfiedDependencyException(ValueSpecifier.of(field.getDeclaringClass()), "not found");
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
