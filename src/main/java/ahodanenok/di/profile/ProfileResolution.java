package ahodanenok.di.profile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface ProfileResolution {

    String resolve(Class<?> clazz);

    String resolve(Field field);

    String resolve(Method method);
}
