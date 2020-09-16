package ahodanenok.di.value;

import ahodanenok.di.*;
import ahodanenok.di.exception.UnsatisfiedDependencyException;
import ahodanenok.di.value.metadata.MutableValueMetadata;

import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldProviderValue<T> extends AbstractValue<T> {

    private final Field field;

    // todo: check field's type is assignable to type

    public FieldProviderValue(Class<T> type, Field field) {
        super(type);
        this.field = field;
        ReflectionAssistant.checkFieldTypeAssignable(type, field);
    }

    @Override
    public Class<? extends T> realType() {
        return (Class<? extends T>) field.getType();
    }

    @Override
    protected MutableValueMetadata resolveMetadata() {
        return container.instance(ValueMetadataResolution.class).resolve(field);
    }

    @Override
    public Provider<? extends T> provider() {
        // todo: suppress unchecked
        if (Modifier.isStatic(field.getModifiers())) {
            return () -> (T) ReflectionAssistant.fieldGet(field, null);
        } else {
            return () -> {
                ValueSpecifier<?> specifier = ValueSpecifier.of(field.getDeclaringClass());
                Object instance = container.instance(specifier);
                if (instance == null) {
                    throw new UnsatisfiedDependencyException(
                            String.format("Couldn't get value of the field '%s' because container doesn't have " +
                                    "an instance of its declaring class '%s'", field, specifier));
                }

                return (T) ReflectionAssistant.fieldGet(field, instance);
            };
        }
    }
}
