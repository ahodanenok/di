package ahodanenok.di.stereotype;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public interface StereotypeResolution {

    /**
     * Returns stereotypes declared for a class
     * Stereotypes returned are in order they are declared on a class
     * @return
     */
    Set<Annotation> resolve(Class<?> clazz);

    /**
     * Returns stereotypes declared for a field
     * Stereotypes returned are in order they are declared on a field
     * @return
     */
    Set<Annotation> resolve(Field field);

    /**
     * Returns stereotypes declared for a method
     * Stereotypes returned are in order they are declared on a method
     * @return
     */
    Set<Annotation> resolve(Method method);
}
