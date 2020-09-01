package ahodanenok.di.name;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// todo: move default name generation to a separate strategy
public interface NameResolution {

    String resolve(Class<?> clazz);

    String resolve(Field field);

    String resolve(Method method);
}
