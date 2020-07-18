package ahodanenok.di;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class ReflectionAssistant {

    private ReflectionAssistant() { }

    public static Stream<Field> fields(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredFields());
        } else {
            return Stream.concat(fields(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredFields()));
        }
    }

    public static Stream<Method> methods(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Arrays.stream(clazz.getDeclaredMethods());
        } else {
            return Stream.concat(methods(clazz.getSuperclass()), Arrays.stream(clazz.getDeclaredMethods()));
        }
    }

    public static Stream<Annotation> scopes(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Scope.class));
    }

    public static Stream<Annotation> parameterAnnotations(Executable executable, int parameterIndex) {
        int parameterCount = executable.getParameterCount();

        if (parameterIndex < 0 || parameterIndex >= parameterCount) {
            throw new IllegalArgumentException(
                    String.format("executable has %d parameter(s), given parameter index: %d", executable.getParameterCount(), parameterIndex));
        }

        int parameterAnnotationsLength = executable.getParameterAnnotations().length;

        int index = parameterIndex;
        if (parameterCount > parameterAnnotationsLength) {
            index -= (parameterCount - parameterAnnotationsLength);
        }

        if (index < 0) {
            return Stream.empty();
        }

        return Arrays.stream(executable.getParameterAnnotations()[index]);
    }

    private static Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = new HashMap<>();
    static {
        PRIMITIVE_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPERS.put(short.class, Short.class);
        PRIMITIVE_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVE_WRAPPERS.put(long.class, Long.class);
        PRIMITIVE_WRAPPERS.put(float.class, Float.class);
        PRIMITIVE_WRAPPERS.put(double.class, Double.class);
        PRIMITIVE_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPERS.put(char.class, Character.class);
    }

    public static Class<?> primitiveWrapperClass(Class<?> clazz) {
        Class<?> wrapper = PRIMITIVE_WRAPPERS.get(clazz);
        if (wrapper == null) {
            throw new IllegalArgumentException("wrapper not found for class " + clazz.getName());
        }

        return wrapper;
    }
}
