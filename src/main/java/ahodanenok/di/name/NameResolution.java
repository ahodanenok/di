package ahodanenok.di.name;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public interface NameResolution {

    String resolve(Class<?> clazz);

    String resolve(Field field);

    String resolve(Method method);
}
