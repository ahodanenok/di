package ahodanenok.di;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
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
}
