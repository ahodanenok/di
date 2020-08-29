package ahodanenok.di.stereotype;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public interface StereotypeResolution {

    /**
     * Returns stereotypes declared for a class
     */
    Set<Annotation> resolve(Class<?> clazz);

    /**
     * Returns stereotypes declared for a field
     */
    Set<Annotation> resolve(Field field);

    /**
     * Returns stereotypes declared for a method
     */
    Set<Annotation> resolve(Method method);

    /**
     * Returns stereotypes declared for a constructor
     */
    Set<Annotation> resolve(Constructor<?> constructor);
}
