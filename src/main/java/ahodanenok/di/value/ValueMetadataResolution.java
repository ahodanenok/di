package ahodanenok.di.value;

import ahodanenok.di.value.metadata.MutableValueMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface ValueMetadataResolution {

    MutableValueMetadata resolve(Class<?> clazz);

    MutableValueMetadata resolve(Method method);

    MutableValueMetadata resolve(Field field);
}
